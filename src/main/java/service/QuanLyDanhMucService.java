package service;

import exception.NghiepVuException;
import org.hibernate.Session;
import util.TransactionHelper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Tập trung nghiệp vụ của 3 màn hình quản trị: loại phòng, mã giảm giá, thanh toán.
 * Dùng native SQL vì các bảng này có nhiều khóa ngoại thuộc các module khác của nhóm.
 * Mỗi thao tác ghi chạy trong một transaction để dữ liệu không bị cập nhật dở dang.
 */
public class QuanLyDanhMucService {

    // =============================== LOẠI PHÒNG ===============================
    public List<Object[]> danhSachLoaiPhong(String tuKhoa) {
        return TransactionHelper.chay(s -> s.createNativeQuery("""
            SELECT lp.id_loai_phong, lp.ten, hp.ma_hang_phong, lg.ten,
                   lp.gia_moi_dem, lp.mo_ta,
                   (SELECT COUNT(*) FROM phong p WHERE p.id_loai_phong = lp.id_loai_phong)
            FROM loai_phong lp
            JOIN hang_phong hp ON hp.id_hang_phong = lp.id_hang_phong
            JOIN loai_giuong lg ON lg.id_loai_giuong = lp.id_loai_giuong
            WHERE (:kw = '' OR lp.ten LIKE :likeKw OR hp.ma_hang_phong LIKE :likeKw)
            ORDER BY lp.id_loai_phong DESC
            """).setParameter("kw", sach(tuKhoa)).setParameter("likeKw", "%" + sach(tuKhoa) + "%").list());
    }

    public List<Object[]> danhSachHangPhong() {
        return TransactionHelper.chay(s -> s.createNativeQuery(
                "SELECT id_hang_phong, ma_hang_phong FROM hang_phong ORDER BY ma_hang_phong").list());
    }

    public List<Object[]> danhSachLoaiGiuong() {
        return TransactionHelper.chay(s -> s.createNativeQuery(
                "SELECT id_loai_giuong, ten, suc_chua FROM loai_giuong ORDER BY ten").list());
    }

    public Object[] layLoaiPhong(long id) {
        List<Object[]> rows = TransactionHelper.chay(s -> s.createNativeQuery("""
            SELECT id_loai_phong, id_hang_phong, id_loai_giuong, ten, gia_moi_dem, mo_ta
            FROM loai_phong WHERE id_loai_phong = :id
            """).setParameter("id", id).list());
        if (rows.isEmpty()) throw new NghiepVuException("Không tìm thấy loại phòng.");
        return rows.get(0);
    }

    public void luuLoaiPhong(Long id, Long idHang, Long idGiuong, String ten, BigDecimal gia, String moTa) {
        String tenSach = batBuoc(ten, "Tên loại phòng");
        if (tenSach.length() > 150) throw new NghiepVuException("Tên loại phòng tối đa 150 ký tự.");
        if (idHang == null || idGiuong == null) throw new NghiepVuException("Vui lòng chọn hạng phòng và loại giường.");
        if (gia == null || gia.compareTo(BigDecimal.ZERO) <= 0) throw new NghiepVuException("Giá mỗi đêm phải lớn hơn 0.");
        if (gia.stripTrailingZeros().scale() > 0) throw new NghiepVuException("Giá mỗi đêm phải là số nguyên VNĐ.");
        if (moTa != null && moTa.length() > 4000) throw new NghiepVuException("Mô tả tối đa 4000 ký tự.");
        TransactionHelper.chayVoid(s -> {
            batBuocTonTai(s, "SELECT COUNT(*) FROM hang_phong WHERE id_hang_phong=:id", idHang, "Hạng phòng không tồn tại.");
            batBuocTonTai(s, "SELECT COUNT(*) FROM loai_giuong WHERE id_loai_giuong=:id", idGiuong, "Loại giường không tồn tại.");
            Number trung = (Number) s.createNativeQuery("""
                SELECT COUNT(*) FROM loai_phong WHERE id_hang_phong=:hang AND id_loai_giuong=:giuong
                AND (:id IS NULL OR id_loai_phong<>:id)
                """).setParameter("hang", idHang).setParameter("giuong", idGiuong).setParameter("id", id == null ? -1L : id).uniqueResult();
            if (trung.longValue() > 0) throw new NghiepVuException("Hạng phòng và loại giường này đã có loại phòng.");
            if (id == null) {
                s.createNativeQuery("""
                    INSERT INTO loai_phong(id_hang_phong,id_loai_giuong,ten,gia_moi_dem,mo_ta)
                    VALUES(:hang,:giuong,:ten,:gia,:mota)
                    """).setParameter("hang", idHang).setParameter("giuong", idGiuong).setParameter("ten", tenSach)
                    .setParameter("gia", gia).setParameter("mota", moTa).executeUpdate();
            } else {
                batBuocTonTai(s, "SELECT COUNT(*) FROM loai_phong WHERE id_loai_phong=:id", id, "Loại phòng không tồn tại.");
                s.createNativeQuery("""
                    UPDATE loai_phong SET id_hang_phong=:hang,id_loai_giuong=:giuong,ten=:ten,gia_moi_dem=:gia,mo_ta=:mota
                    WHERE id_loai_phong=:id
                    """).setParameter("hang", idHang).setParameter("giuong", idGiuong).setParameter("ten", tenSach)
                    .setParameter("gia", gia).setParameter("mota", moTa).setParameter("id", id).executeUpdate();
            }
        });
    }

    public void xoaLoaiPhong(long id) {
        TransactionHelper.chayVoid(s -> {
            Number count = (Number) s.createNativeQuery("SELECT COUNT(*) FROM phong WHERE id_loai_phong=:id").setParameter("id", id).uniqueResult();
            if (count.longValue() > 0) throw new NghiepVuException("Không thể xóa: loại phòng đang được sử dụng bởi " + count + " phòng.");
            int n = s.createNativeQuery("DELETE FROM loai_phong WHERE id_loai_phong=:id").setParameter("id", id).executeUpdate();
            if (n == 0) throw new NghiepVuException("Loại phòng không tồn tại.");
        });
    }

    // =============================== MÃ GIẢM GIÁ ===============================
    public List<Object[]> danhSachMaGiamGia(String tuKhoa) {
        return TransactionHelper.chay(s -> s.createNativeQuery("""
            SELECT m.id_ma_giam_gia,m.ma_giam_gia,m.loai_giam_gia,m.gia_tri,m.bat_dau_luc,m.ket_thuc_luc,
                   m.so_luot_da_dung,m.trang_thai,COUNT(d.id_dat_phong)
            FROM ma_giam_gia m LEFT JOIN dat_phong d ON d.id_ma_giam_gia=m.id_ma_giam_gia
            WHERE (:kw='' OR m.ma_giam_gia LIKE :likeKw)
            GROUP BY m.id_ma_giam_gia,m.ma_giam_gia,m.loai_giam_gia,m.gia_tri,m.bat_dau_luc,m.ket_thuc_luc,m.so_luot_da_dung,m.trang_thai
            ORDER BY m.id_ma_giam_gia DESC
            """).setParameter("kw", sach(tuKhoa)).setParameter("likeKw", "%"+sach(tuKhoa)+"%").list());
    }

    public Object[] layMaGiamGia(long id) {
        List<Object[]> rows = TransactionHelper.chay(s -> s.createNativeQuery("""
            SELECT id_ma_giam_gia,ma_giam_gia,loai_giam_gia,gia_tri,CONVERT(varchar(16),bat_dau_luc,126),CONVERT(varchar(16),ket_thuc_luc,126),trang_thai
            FROM ma_giam_gia WHERE id_ma_giam_gia=:id
            """).setParameter("id", id).list());
        if (rows.isEmpty()) throw new NghiepVuException("Không tìm thấy mã giảm giá.");
        return rows.get(0);
    }

    public void luuMaGiamGia(Long id, String ma, String loai, BigDecimal giaTri,
                             LocalDateTime batDau, LocalDateTime ketThuc, String trangThai) {
        String code = batBuoc(ma, "Mã giảm giá").toUpperCase();
        if (!code.matches("[A-Z0-9_-]{3,30}")) throw new NghiepVuException("Mã giảm giá dài 3-30 ký tự, chỉ gồm chữ in hoa, số, _ hoặc -.");
        if (!"PHAN_TRAM".equals(loai) && !"SO_TIEN".equals(loai)) throw new NghiepVuException("Loại giảm giá không hợp lệ.");
        if (giaTri == null || giaTri.compareTo(BigDecimal.ZERO) <= 0) throw new NghiepVuException("Giá trị giảm phải lớn hơn 0.");
        if ("PHAN_TRAM".equals(loai) && giaTri.compareTo(new BigDecimal("100")) > 0)
            throw new NghiepVuException("Giảm theo phần trăm phải từ lớn hơn 0 đến tối đa 100%.");
        if (batDau == null || ketThuc == null || !ketThuc.isAfter(batDau)) throw new NghiepVuException("Thời điểm kết thúc phải sau thời điểm bắt đầu.");
        if (!"HOAT_DONG".equals(trangThai) && !"NGUNG".equals(trangThai)) throw new NghiepVuException("Trạng thái mã giảm giá không hợp lệ.");
        TransactionHelper.chayVoid(s -> {
            Number trung = (Number) s.createNativeQuery("SELECT COUNT(*) FROM ma_giam_gia WHERE ma_giam_gia=:ma AND (:id IS NULL OR id_ma_giam_gia<>:id)")
                    .setParameter("ma", code).setParameter("id", id == null ? -1L : id).uniqueResult();
            if (trung.longValue()>0) throw new NghiepVuException("Mã giảm giá đã tồn tại.");
            if (id == null) {
                s.createNativeQuery("""
                    INSERT INTO ma_giam_gia(ma_giam_gia,loai_giam_gia,gia_tri,bat_dau_luc,ket_thuc_luc,trang_thai)
                    VALUES(:ma,:loai,:gia,:bd,:kt,:tt)
                    """).setParameter("ma",code).setParameter("loai",loai).setParameter("gia",giaTri)
                    .setParameter("bd",batDau).setParameter("kt",ketThuc).setParameter("tt",trangThai).executeUpdate();
            } else {
                batBuocTonTai(s,"SELECT COUNT(*) FROM ma_giam_gia WHERE id_ma_giam_gia=:id",id,"Mã giảm giá không tồn tại.");
                s.createNativeQuery("""
                    UPDATE ma_giam_gia SET ma_giam_gia=:ma,loai_giam_gia=:loai,gia_tri=:gia,
                    bat_dau_luc=:bd,ket_thuc_luc=:kt,trang_thai=:tt WHERE id_ma_giam_gia=:id
                    """).setParameter("ma",code).setParameter("loai",loai).setParameter("gia",giaTri)
                    .setParameter("bd",batDau).setParameter("kt",ketThuc).setParameter("tt",trangThai).setParameter("id",id).executeUpdate();
            }
        });
    }

    public void xoaMaGiamGia(long id) {
        TransactionHelper.chayVoid(s -> {
            Number used=(Number)s.createNativeQuery("SELECT COUNT(*) FROM dat_phong WHERE id_ma_giam_gia=:id").setParameter("id",id).uniqueResult();
            if(used.longValue()>0) throw new NghiepVuException("Không thể xóa mã đã được gắn vào đơn đặt phòng. Hãy chuyển trạng thái sang NGUNG.");
            int n=s.createNativeQuery("DELETE FROM ma_giam_gia WHERE id_ma_giam_gia=:id").setParameter("id",id).executeUpdate();
            if(n==0) throw new NghiepVuException("Mã giảm giá không tồn tại.");
        });
    }

    // =============================== THANH TOÁN ===============================
    public List<Object[]> danhSachThanhToan(String tuKhoa, String trangThai) {
        return TransactionHelper.chay(s -> s.createNativeQuery("""
            SELECT tt.id_thanh_toan,dp.ma_dat_phong,kh.ho_ten,tt.phuong_thuc_thanh_toan,
                   tt.tien_thanh_toan,tt.trang_thai,dp.id_dat_phong
            FROM thanh_toan tt JOIN dat_phong dp ON dp.id_dat_phong=tt.id_dat_phong
            JOIN khach_hang kh ON kh.id_khach_hang=dp.id_khach_hang
            WHERE (:kw='' OR dp.ma_dat_phong LIKE :likeKw OR kh.ho_ten LIKE :likeKw)
              AND (:tt='' OR tt.trang_thai=:tt)
            ORDER BY tt.id_thanh_toan DESC
            """).setParameter("kw",sach(tuKhoa)).setParameter("likeKw","%"+sach(tuKhoa)+"%")
                .setParameter("tt",sach(trangThai)).list());
    }

    public List<Object[]> danhSachDatPhong() {
        return TransactionHelper.chay(s -> s.createNativeQuery("""
            SELECT dp.id_dat_phong,dp.ma_dat_phong,kh.ho_ten,dp.tong_tien,dp.tien_thanh_toan,dp.trang_thai
            FROM dat_phong dp JOIN khach_hang kh ON kh.id_khach_hang=dp.id_khach_hang
            WHERE dp.trang_thai NOT IN ('KHACH_HUY','KHACH_SAN_HUY','TU_HUY') ORDER BY dp.id_dat_phong DESC
            """).list());
    }

    public void taoThanhToan(Long idDatPhong, Long idThe, String phuongThuc, BigDecimal soTien) {
        if(idDatPhong==null) throw new NghiepVuException("Vui lòng chọn đơn đặt phòng.");
        if(!List.of("TIEN_MAT","VNPAY","THE").contains(phuongThuc)) throw new NghiepVuException("Phương thức thanh toán không hợp lệ.");
        if(soTien==null || soTien.compareTo(BigDecimal.ZERO)<=0) throw new NghiepVuException("Số tiền thanh toán phải lớn hơn 0.");
        if(soTien.stripTrailingZeros().scale()>0) throw new NghiepVuException("Số tiền thanh toán phải là số nguyên VNĐ.");
        if("THE".equals(phuongThuc) && idThe==null) throw new NghiepVuException("Thanh toán bằng thẻ thành viên bắt buộc chọn thẻ thành viên.");
        TransactionHelper.chayVoid(s -> {
            List<Object[]> dp=s.createNativeQuery("SELECT tong_tien,tien_thanh_toan,trang_thai FROM dat_phong WHERE id_dat_phong=:id")
                    .setParameter("id",idDatPhong).list();
            if(dp.isEmpty()) throw new NghiepVuException("Đơn đặt phòng không tồn tại.");
            Object[] row=dp.get(0);
            if(List.of("KHACH_HUY","KHACH_SAN_HUY","TU_HUY").contains(row[2].toString())) throw new NghiepVuException("Không thể thanh toán cho đơn đã hủy.");
            BigDecimal conLai=((BigDecimal)row[0]).subtract((BigDecimal)row[1]);
            if(soTien.compareTo(conLai)>0) throw new NghiepVuException("Số tiền vượt quá số tiền còn phải thanh toán: "+conLai.toPlainString()+".");
            if("THE".equals(phuongThuc)) {
                Number the=(Number)s.createNativeQuery("SELECT COUNT(*) FROM the_thanh_vien WHERE id_the_thanh_vien=:id AND trang_thai='HOAT_DONG'")
                        .setParameter("id",idThe).uniqueResult();
                if(the.longValue()==0) throw new NghiepVuException("Thẻ thành viên không tồn tại hoặc không hoạt động.");
            }
            s.createNativeQuery("INSERT INTO thanh_toan(id_dat_phong,id_the_thanh_vien,phuong_thuc_thanh_toan,tien_thanh_toan,trang_thai) VALUES(:dp,:the,:pt,:tien,'CHO_XU_LY')")
                    .setParameter("dp",idDatPhong).setParameter("the",idThe).setParameter("pt",phuongThuc).setParameter("tien",soTien).executeUpdate();
        });
    }

    /** Chỉ cộng vào số đã thanh toán khi chuyển từ CHO_XU_LY sang THANH_CONG. */
    public void capNhatTrangThaiThanhToan(long id, String trangThaiMoi) {
        if(!List.of("THANH_CONG","THAT_BAI","DA_HOAN").contains(trangThaiMoi)) throw new NghiepVuException("Trạng thái thanh toán không hợp lệ.");
        TransactionHelper.chayVoid(s -> {
            List<Object[]> rows=s.createNativeQuery("SELECT id_dat_phong,tien_thanh_toan,trang_thai FROM thanh_toan WHERE id_thanh_toan=:id")
                    .setParameter("id",id).list();
            if(rows.isEmpty()) throw new NghiepVuException("Không tìm thấy giao dịch.");
            Object[] tt=rows.get(0); String cu=tt[2].toString();
            if(cu.equals(trangThaiMoi)) return;
            BigDecimal amount=(BigDecimal)tt[1];
            if("CHO_XU_LY".equals(cu) && "THANH_CONG".equals(trangThaiMoi)) {
                // Ghi nhận tiền vào đơn cùng transaction với cập nhật trạng thái giao dịch.
                int n=s.createNativeQuery("UPDATE dat_phong SET tien_thanh_toan=tien_thanh_toan+:tien WHERE id_dat_phong=:dp AND tien_thanh_toan+:tien<=tong_tien")
                        .setParameter("tien",amount).setParameter("dp",tt[0]).executeUpdate();
                if(n==0) throw new NghiepVuException("Không thể xác nhận: tổng thanh toán sẽ vượt tổng tiền đơn.");
            } else if("CHO_XU_LY".equals(cu) && "THAT_BAI".equals(trangThaiMoi)) {
                // Giao dịch thất bại không làm thay đổi số tiền đã thanh toán.
            } else if("THANH_CONG".equals(cu) && "DA_HOAN".equals(trangThaiMoi)) {
                // Đánh dấu hoàn tiền nội bộ: trừ khoản đã hoàn khỏi số tiền đơn đã thu.
                int n=s.createNativeQuery("UPDATE dat_phong SET tien_thanh_toan=tien_thanh_toan-:tien WHERE id_dat_phong=:dp AND tien_thanh_toan>=:tien")
                        .setParameter("tien",amount).setParameter("dp",tt[0]).executeUpdate();
                if(n==0) throw new NghiepVuException("Không thể ghi nhận hoàn tiền do số tiền đã thanh toán không đủ.");
            } else {
                throw new NghiepVuException("Chuyển trạng thái không hợp lệ. Chỉ cho phép CHO_XU_LY → THANH_CONG/THAT_BAI hoặc THANH_CONG → DA_HOAN.");
            }
            s.createNativeQuery("UPDATE thanh_toan SET trang_thai=:tt WHERE id_thanh_toan=:id")
                    .setParameter("tt",trangThaiMoi).setParameter("id",id).executeUpdate();
        });
    }

    private static String sach(String s){ return s==null?"":s.trim(); }
    private static String batBuoc(String s,String ten){ if(s==null||s.isBlank()) throw new NghiepVuException(ten+" không được để trống."); return s.trim().replaceAll("\\s+"," "); }
    private static void batBuocTonTai(Session s,String sql,Long id,String message){
        Number n=(Number)s.createNativeQuery(sql).setParameter("id",id).uniqueResult();
        if(n.longValue()==0) throw new NghiepVuException(message);
    }
}
