package repository;

import entity.*;
import jakarta.persistence.LockModeType;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DatPhongRepository {

    /** Trạng thái dat_phong_chi_tiet đang "chiếm" phòng (khớp với trigger trg_dat_phong_chi_tiet_overlap). */
    private static final List<String> TRANG_THAI_CHIEM_PHONG =
            List.of(DatPhongChiTiet.HIEU_LUC, DatPhongChiTiet.DANG_LUU_TRU);

    // ------------------------------------------------------------------ TRA CỨU PHÒNG

    /**
     * Phòng còn trống trong khoảng [nhan, tra): không bảo trì, đủ sức chứa, và KHÔNG có chi tiết đặt
     * nào đang hiệu lực giao nhau. Điều kiện giao nhau y hệt trigger: d.nhan < tra AND d.tra > nhan.
     */
    public List<Phong> timPhongTrong(Session s, LocalDate nhan, LocalDate tra,
                                     Long idHangPhong, Long idLoaiGiuong, int soNguoiToiThieu) {
        StringBuilder hql = new StringBuilder("""
                select p from Phong p
                  join fetch p.loaiPhong lp
                  join fetch lp.hangPhong hp
                  join fetch lp.loaiGiuong lg
                where p.trangThai <> :baoTri
                  and lg.sucChua >= :soNguoi
                  and not exists (
                      select 1 from DatPhongChiTiet d
                      where d.phong = p
                        and d.trangThai in :chiemPhong
                        and d.ngayNhan < :tra
                        and d.ngayTra > :nhan)
                """);
        if (idHangPhong != null)  hql.append(" and hp.id = :idHang");
        if (idLoaiGiuong != null) hql.append(" and lg.id = :idGiuong");
        hql.append(" order by lp.giaMoiDem, p.maPhong");

        var q = s.createQuery(hql.toString(), Phong.class)
                .setParameter("baoTri", Phong.BAO_TRI)
                .setParameter("soNguoi", soNguoiToiThieu)
                .setParameter("chiemPhong", TRANG_THAI_CHIEM_PHONG)
                .setParameter("nhan", nhan)
                .setParameter("tra", tra);
        if (idHangPhong != null)  q.setParameter("idHang", idHangPhong);
        if (idLoaiGiuong != null) q.setParameter("idGiuong", idLoaiGiuong);
        return q.getResultList();
    }

    public List<Phong> timPhongTheoDanhSachId(Session s, List<Long> ids) {
        if (ids.isEmpty()) return List.of();
        return s.createQuery("""
                        select p from Phong p
                          join fetch p.loaiPhong lp
                          join fetch lp.hangPhong
                          join fetch lp.loaiGiuong
                        where p.id in :ids order by p.maPhong
                        """, Phong.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    /** Lấy phòng + khóa dòng (UPDLOCK) để 2 người không đặt cùng 1 phòng cùng lúc. */
    public Phong timPhongVaKhoa(Session s, Long idPhong) {
        return s.find(Phong.class, idPhong, LockModeType.PESSIMISTIC_WRITE);
    }

    public boolean phongBiTrung(Session s, Long idPhong, LocalDate nhan, LocalDate tra) {
        Long dem = s.createQuery("""
                        select count(d) from DatPhongChiTiet d
                        where d.phong.id = :id and d.trangThai in :chiemPhong
                          and d.ngayNhan < :tra and d.ngayTra > :nhan
                        """, Long.class)
                .setParameter("id", idPhong)
                .setParameter("chiemPhong", TRANG_THAI_CHIEM_PHONG)
                .setParameter("nhan", nhan)
                .setParameter("tra", tra)
                .getSingleResult();
        return dem > 0;
    }

    public List<HangPhong> layHangPhong(Session s) {
        return s.createQuery("from HangPhong h order by h.id", HangPhong.class).getResultList();
    }

    public List<LoaiGiuong> layLoaiGiuong(Session s) {
        return s.createQuery("from LoaiGiuong g order by g.sucChua, g.id", LoaiGiuong.class).getResultList();
    }

    // ------------------------------------------------------------------ KHÁCH HÀNG (chỉ để chọn khi đặt)

    public List<KhachHang> timKhachHang(Session s, String tuKhoa) {
        boolean coLoc = tuKhoa != null && !tuKhoa.isBlank();
        String hql = "from KhachHang k where k.trangThai = 'HOAT_DONG'"
                + (coLoc ? " and (lower(k.hoTen) like :kw or k.soDienThoai like :kw)" : "")
                + " order by k.hoTen";
        var q = s.createQuery(hql, KhachHang.class);
        if (coLoc) q.setParameter("kw", "%" + tuKhoa.trim().toLowerCase() + "%");
        return q.setMaxResults(50).getResultList();
    }

    // ------------------------------------------------------------------ MÃ GIẢM GIÁ / HẠNG THÀNH VIÊN (native SQL để không đụng entity của người khác)

    /** @return {id, loai_giam_gia, gia_tri} nếu mã đang hiệu lực, ngược lại null. */
    public Object[] timMaGiamGiaHopLe(Session s, String ma, LocalDateTime bayGio) {
        List<?> rs = s.createNativeQuery("""
                        SELECT id_ma_giam_gia, loai_giam_gia, gia_tri
                        FROM ma_giam_gia
                        WHERE ma_giam_gia = :ma AND trang_thai = 'HOAT_DONG'
                          AND bat_dau_luc <= :now AND ket_thuc_luc >= :now
                        """)
                .setParameter("ma", ma)
                .setParameter("now", bayGio)
                .getResultList();
        return rs.isEmpty() ? null : (Object[]) rs.get(0);
    }

    public void tangLuotDungMaGiamGia(Session s, Long idMa) {
        s.createNativeQuery("UPDATE ma_giam_gia SET so_luot_da_dung = so_luot_da_dung + 1 WHERE id_ma_giam_gia = :id")
                .setParameter("id", idMa)
                .executeUpdate();
    }

    /** % giảm giá phòng theo hạng thẻ thành viên đang hoạt động của khách (0 nếu không có thẻ). */
    public int phanTramGiamHangThanhVien(Session s, Long idKhachHang) {
        List<?> rs = s.createNativeQuery("""
                        SELECT h.phan_tram_giam_gia_phong
                        FROM the_thanh_vien t
                        JOIN hang_thanh_vien h ON h.id_hang_thanh_vien = t.id_hang_thanh_vien
                        WHERE t.id_khach_hang = :id AND t.trang_thai = 'HOAT_DONG'
                        """)
                .setParameter("id", idKhachHang)
                .getResultList();
        return rs.isEmpty() ? 0 : ((Number) rs.get(0)).intValue();
    }

    // ------------------------------------------------------------------ ĐẶT PHÒNG

    /** Sinh mã dạng DP2026001, DP2026002... (cùng format seed data). UNIQUE trong DB là chốt chặn cuối. */
    public String sinhMaDatPhong(Session s) {
        String tienTo = "DP" + LocalDate.now().getYear();
        List<String> rs = s.createQuery("""
                        select d.maDatPhong from DatPhong d where d.maDatPhong like :p
                        order by length(d.maDatPhong) desc, d.maDatPhong desc
                        """, String.class)
                .setParameter("p", tienTo + "%")
                .setMaxResults(1)
                .getResultList();
        int tiepTheo = rs.isEmpty() ? 1 : Integer.parseInt(rs.get(0).substring(tienTo.length())) + 1;
        return tienTo + String.format("%03d", tiepTheo);
    }

    public void luuDatPhong(Session s, DatPhong dp) { s.persist(dp); }

    // ------------------------------------------------------------------ TRA CỨU ĐẶT PHÒNG

    /** Tất cả điều kiện đều tùy chọn (null = bỏ qua). Lọc ngày theo ngày nhận phòng. */
    public List<DatPhong> timKiem(Session s, String maDatPhong, String tuKhoaKhach, String trangThai,
                                  LocalDate tuNgay, LocalDate denNgay) {
        StringBuilder hql = new StringBuilder(
                "select d from DatPhong d join fetch d.khachHang k where 1 = 1");
        if (maDatPhong != null)  hql.append(" and lower(d.maDatPhong) like :ma");
        if (tuKhoaKhach != null) hql.append(" and (lower(k.hoTen) like :kh or k.soDienThoai like :kh)");
        if (trangThai != null)   hql.append(" and d.trangThai = :tt");
        if (tuNgay != null)      hql.append(" and d.thoiGianNhanPhong >= :tu");
        if (denNgay != null)     hql.append(" and d.thoiGianNhanPhong < :den");
        hql.append(" order by d.id desc");

        var q = s.createQuery(hql.toString(), DatPhong.class);
        if (maDatPhong != null)  q.setParameter("ma", "%" + maDatPhong.toLowerCase() + "%");
        if (tuKhoaKhach != null) q.setParameter("kh", "%" + tuKhoaKhach.toLowerCase() + "%");
        if (trangThai != null)   q.setParameter("tt", trangThai);
        if (tuNgay != null)      q.setParameter("tu", tuNgay.atStartOfDay());
        if (denNgay != null)     q.setParameter("den", denNgay.plusDays(1).atStartOfDay());
        return q.getResultList();
    }

    /** Đơn + khách + các phòng (kèm loại phòng) - fetch hết để JSP dùng được sau khi Session đóng. */
    public DatPhong timTheoIdDayDu(Session s, Long id) {
        List<DatPhong> rs = s.createQuery("""
                        select distinct d from DatPhong d
                          join fetch d.khachHang
                          left join fetch d.chiTiet c
                          left join fetch c.phong p
                          left join fetch p.loaiPhong
                        where d.id = :id
                        """, DatPhong.class)
                .setParameter("id", id)
                .getResultList();
        return rs.isEmpty() ? null : rs.get(0);
    }

    // ------------------------------------------------------------------ HỦY PHÒNG

    public DatPhong timVaKhoaDatPhong(Session s, Long id) {
        return s.find(DatPhong.class, id, LockModeType.PESSIMISTIC_WRITE);
    }

    public void danhDauHuyChiTiet(Session s, Long idDatPhong) {
        s.createMutationQuery("update DatPhongChiTiet c set c.trangThai = :tt where c.datPhong.id = :id")
                .setParameter("tt", DatPhongChiTiet.DA_HUY)
                .setParameter("id", idDatPhong)
                .executeUpdate();
    }

    public void luuLichSuHuy(Session s, LichSuDatPhong ls) { s.persist(ls); }

    public LichSuDatPhong timLichSuHuy(Session s, Long idDatPhong) {
        List<LichSuDatPhong> rs = s.createQuery("from LichSuDatPhong l where l.idDatPhong = :id", LichSuDatPhong.class)
                .setParameter("id", idDatPhong)
                .getResultList();
        return rs.isEmpty() ? null : rs.get(0);
    }
}
