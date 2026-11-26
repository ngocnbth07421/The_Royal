package repository;

import entity.*;
import controller.NghiepVuException;
import jakarta.persistence.LockModeType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
        String hql = "select k from KhachHang k left join fetch k.nguoiDung nd where (nd is null or nd.trangThai in ('HOAT_DONG', N'Hoạt động'))"
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
                        WHERE ma_giam_gia = :ma AND trang_thai IN (N'Hoạt động', 'HOAT_DONG')
                          AND so_luot_con_dung > 0
                          AND bat_dau_luc <= :now AND ket_thuc_luc >= :now
                        """)
                .setParameter("ma", ma)
                .setParameter("now", bayGio)
                .getResultList();
        return rs.isEmpty() ? null : (Object[]) rs.get(0);
    }

    public void tangLuotDungMaGiamGia(Session s, Long idMa) {
        s.createNativeQuery("UPDATE ma_giam_gia SET so_luot_con_dung = CASE WHEN so_luot_con_dung > 0 THEN so_luot_con_dung - 1 ELSE 0 END WHERE id_ma_giam_gia = :id")
                .setParameter("id", idMa)
                .executeUpdate();
    }

    /** % giảm giá phòng theo hạng thẻ thành viên đang hoạt động của khách (0 nếu không có thẻ). */
    public int phanTramGiamHangThanhVien(Session s, Long idKhachHang) {
        List<?> rs = s.createNativeQuery("""
                        SELECT h.phan_tram_giam_gia_phong
                        FROM the_thanh_vien t
                        JOIN hang_thanh_vien h ON h.id_hang_thanh_vien = t.id_hang_thanh_vien
                        WHERE t.id_khach_hang = :id AND t.trang_thai IN (N'Hoạt động', 'HOAT_DONG')
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
                "select distinct d from DatPhong d join fetch d.khachHang k left join fetch d.chiTiet c where 1 = 1");
        if (maDatPhong != null)  hql.append(" and lower(d.maDatPhong) like :ma");
        if (tuKhoaKhach != null) hql.append(" and (lower(k.hoTen) like :kh or k.soDienThoai like :kh)");
        if (trangThai != null)   hql.append(" and exists (select 1 from DatPhongChiTiet ct where ct.datPhong = d and ct.trangThai = :tt)");
        if (tuNgay != null)      hql.append(" and d.thoiGianNhanPhong >= :tu");
        if (denNgay != null)     hql.append(" and d.thoiGianNhanPhong < :den");
        hql.append(" order by d.id desc");

        var q = s.createQuery(hql.toString(), DatPhong.class);
        if (maDatPhong != null)  q.setParameter("ma", "%" + maDatPhong.toLowerCase() + "%");
        if (tuKhoaKhach != null) q.setParameter("kh", "%" + tuKhoaKhach.toLowerCase() + "%");
        if (trangThai != null)   q.setParameter("tt", chuanHoaTrangThai(trangThai));
        if (tuNgay != null)      q.setParameter("tu", tuNgay.atStartOfDay());
        if (denNgay != null)     q.setParameter("den", denNgay.plusDays(1).atStartOfDay());
        return q.getResultList();
    }

    private String chuanHoaTrangThai(String tt) {
        if (tt == null) return null;
        return switch (tt) {
            case "CHO_COC", "TAM_GIU" -> DatPhong.TAM_GIU;
            case "DA_XAC_NHAN" -> DatPhong.DA_XAC_NHAN;
            case "DANG_LUU_TRU", "KHACH_DANG_O" -> DatPhong.KHACH_DANG_O;
            case "DA_TRA_PHONG" -> DatPhong.DA_TRA_PHONG;
            case "KHACH_HUY", "KHACH_SAN_HUY", "DA_HUY" -> DatPhong.DA_HUY;
            case "TU_HUY", "KHONG_DEN" -> DatPhong.KHONG_DEN;
            case "KHONG_THANH_CONG" -> DatPhong.KHONG_THANH_CONG;
            default -> tt;
        };
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
        s.createQuery("update DatPhongChiTiet c set c.trangThai = :tt where c.datPhong.id = :id")
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

    // ------------------------------------------------------------------ LỊCH ĐẶT PHÒNG

    /**
     * Các lượt ở (đơn x phòng) giao với khoảng [tu, den). Dùng cho lịch kiểu Google Calendar.
     * maPhong / trangThaiDon có thể null. goiHuy = false thì bỏ các lượt đã hủy.
     */
    public List<DatPhongChiTiet> timLichTheoKhoang(Session s, LocalDate tu, LocalDate den,
                                                   String maPhong, String trangThaiDon, boolean goiHuy) {
        StringBuilder hql = new StringBuilder("""
                select c from DatPhongChiTiet c
                  join fetch c.datPhong d
                  join fetch d.khachHang k
                  join fetch c.phong p
                where c.ngayNhan < :den and c.ngayTra > :tu
                """);
        if (!goiHuy)            hql.append(" and c.trangThai <> :huy");
        if (maPhong != null)    hql.append(" and lower(p.maPhong) like :ma");
        if (trangThaiDon != null) hql.append(" and c.trangThai = :tt");
        hql.append(" order by c.ngayNhan, p.maPhong");

        var q = s.createQuery(hql.toString(), DatPhongChiTiet.class)
                .setParameter("tu", tu)
                .setParameter("den", den);
        if (!goiHuy)            q.setParameter("huy", DatPhongChiTiet.DA_HUY);
        if (maPhong != null)    q.setParameter("ma", "%" + maPhong.toLowerCase() + "%");
        if (trangThaiDon != null) q.setParameter("tt", trangThaiDon);
        return q.getResultList();
    }

    // ------------------------------------------------------------------ NGHIỆP VỤ ĐẶT PHÒNG
    static final BigDecimal TY_LE_VAT = new BigDecimal("0.10");
    static final BigDecimal TY_LE_COC = new BigDecimal("0.30");
    static final LocalTime  GIO_NHAN_PHONG = LocalTime.of(14, 0);
    static final LocalTime  GIO_TRA_PHONG  = LocalTime.of(12, 0);
    static final int HOAN_DAY_DU_TRUOC_GIO = 48;
    static final int HOAN_MOT_NUA_TRUOC_GIO = 24;

    public List<Phong> traCuuPhongTrong(LocalDate nhan, LocalDate tra, Long idHangPhong,
                                        Long idLoaiGiuong, int soNguoi) {
        kiemTraKhoangNgay(nhan, tra);
        if (soNguoi < 1) throw new NghiepVuException("Số người phải >= 1.");
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            return timPhongTrong(s, nhan, tra, idHangPhong, idLoaiGiuong, soNguoi);
        }
    }

    public List<HangPhong> layHangPhong() {
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            return layHangPhong(s);
        }
    }

    public List<LoaiGiuong> layLoaiGiuong() {
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            return layLoaiGiuong(s);
        }
    }

    public List<KhachHang> timKhachHang(String tuKhoa) {
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            return timKhachHang(s, tuKhoa);
        }
    }

    public List<Phong> layPhongDeDat(List<Long> ids) {
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            return timPhongTheoDanhSachId(s, ids);
        }
    }

    public DatPhong datPhong(YeuCauDatPhong yc) {
        kiemTraYeuCau(yc);
        Transaction tx = null;
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            tx = s.beginTransaction();
            DatPhong dp = thucHienDatPhong(s, yc);
            tx.commit();
            return dp;
        } catch (NghiepVuException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw chuyenLoiTrigger(e);
        }
    }

    private DatPhong thucHienDatPhong(Session s, YeuCauDatPhong yc) {
        KhachHang kh = s.find(KhachHang.class, yc.idKhachHang());
        if (kh == null || ("BI_KHOA".equals(kh.getTrangThai()) || "Bị khóa".equals(kh.getTrangThai())))
            throw new NghiepVuException("Khách hàng không tồn tại hoặc đang bị khóa.");

        int soDem = (int) ChronoUnit.DAYS.between(yc.ngayNhan(), yc.ngayTra());

        DatPhong dp = new DatPhong();
        dp.setKhachHang(kh);
        dp.setIdNguoiTao(yc.idNguoiTao());
        dp.setThoiGianNhanPhong(yc.ngayNhan().atTime(GIO_NHAN_PHONG));
        dp.setThoiGianTraPhong(yc.ngayTra().atTime(GIO_TRA_PHONG));
        dp.setSoDem(soDem);

        BigDecimal tienPhong = BigDecimal.ZERO;
        List<PhongYeuCau> dsPhong = yc.phongs().stream()
                .sorted(Comparator.comparing(PhongYeuCau::idPhong)).toList();
        for (PhongYeuCau pyc : dsPhong) {
            Phong p = timPhongVaKhoa(s, pyc.idPhong());
            if (p == null) throw new NghiepVuException("Phòng id=" + pyc.idPhong() + " không tồn tại.");
            if (Phong.BAO_TRI.equals(p.getTrangThai()))
                throw new NghiepVuException("Phòng " + p.getMaPhong() + " đang bảo trì.");

            int sucChua = p.getLoaiPhong().getLoaiGiuong().getSucChua();
            if (pyc.soNguoiLon() + pyc.soTreEmTu6Tuoi() > sucChua)
                throw new NghiepVuException("Phòng " + p.getMaPhong() + " chỉ chứa tối đa " + sucChua
                        + " người (người lớn + trẻ từ 6 tuổi).");
            if (phongBiTrung(s, p.getId(), yc.ngayNhan(), yc.ngayTra()))
                throw new NghiepVuException("Phòng " + p.getMaPhong() + " đã có người đặt trong khoảng thời gian này.");

            DatPhongChiTiet ct = new DatPhongChiTiet();
            ct.setDatPhong(dp);
            ct.setPhong(p);
            ct.setSoNguoiLon(pyc.soNguoiLon());
            ct.setSoTreEmDuoi6(pyc.soTreEmDuoi6());
            ct.setSoTreEmTu6Tuoi(pyc.soTreEmTu6Tuoi());
            ct.setNgayNhan(yc.ngayNhan());
            ct.setNgayTra(yc.ngayTra());
            dp.getChiTiet().add(ct);

            tienPhong = tienPhong.add(p.getLoaiPhong().getGiaMoiDem().multiply(BigDecimal.valueOf(soDem)));
        }

        BigDecimal giamGia = phanTram(tienPhong, phanTramGiamHangThanhVien(s, kh.getId()));
        if (yc.maGiamGia() != null && !yc.maGiamGia().isBlank()) {
            Object[] mg = timMaGiamGiaHopLe(s, yc.maGiamGia().trim(), LocalDateTime.now());
            if (mg == null) throw new NghiepVuException("Mã giảm giá không tồn tại, đã hết hạn hoặc đã ngừng.");
            Long idMa = ((Number) mg[0]).longValue();
            BigDecimal giaTri = new BigDecimal(mg[2].toString());
            String loai = mg[1].toString();
            BigDecimal giamTuMa = ("PHAN_TRAM".equals(loai) || "Phần trăm".equals(loai))
                    ? tienPhong.multiply(giaTri).divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP)
                    : giaTri.setScale(0, RoundingMode.HALF_UP);
            giamGia = giamGia.add(giamTuMa);
            dp.setIdMaGiamGia(idMa);
            tangLuotDungMaGiamGia(s, idMa);
        }
        giamGia = giamGia.min(tienPhong);

        BigDecimal phiDichVu = BigDecimal.ZERO;
        BigDecimal truocThue = tienPhong.subtract(giamGia).add(phiDichVu);
        BigDecimal thue = truocThue.multiply(TY_LE_VAT).setScale(0, RoundingMode.HALF_UP);
        BigDecimal tong = truocThue.add(thue);

        dp.setMaDatPhong(sinhMaDatPhong(s));
        dp.setTrangThai(DatPhong.CHO_COC);
        dp.setTienPhong(tienPhong);
        dp.setGiamGia(giamGia);
        dp.setPhiDichVu(phiDichVu);
        dp.setThueVat(thue);
        dp.setTongTien(tong);
        dp.setTienCoc(tong.multiply(TY_LE_COC).setScale(0, RoundingMode.HALF_UP));

        luuDatPhong(s, dp);
        return dp;
    }

    public LichSuDatPhong huyDatPhong(Long idDatPhong, String loaiHuy, Long idNguoiHuy, String ghiChu) {
        if (!DatPhong.KHACH_HUY.equals(loaiHuy) && !DatPhong.KHACH_SAN_HUY.equals(loaiHuy))
            throw new NghiepVuException("Loại hủy không hợp lệ.");
        if (ghiChu != null && ghiChu.length() > 255) throw new NghiepVuException("Ghi chú tối đa 255 ký tự.");

        Transaction tx = null;
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            tx = s.beginTransaction();
            DatPhong dp = timVaKhoaDatPhong(s, idDatPhong);
            if (dp == null) throw new NghiepVuException("Không tìm thấy đơn đặt phòng.");
            if (!DatPhong.CHO_COC.equals(dp.getTrangThai()) && !DatPhong.DA_XAC_NHAN.equals(dp.getTrangThai()))
                throw new NghiepVuException("Chỉ hủy được đơn đang 'Chờ cọc' hoặc 'Đã xác nhận' (hiện tại: "
                        + dp.getTrangThai() + ").");

            BigDecimal hoan = tinhTienHoan(dp, loaiHuy);

            dp.setTrangThai(loaiHuy);
            danhDauHuyChiTiet(s, dp.getId());

            LichSuDatPhong ls = new LichSuDatPhong();
            ls.setIdDatPhong(dp.getId());
            ls.setIdNguoiHuy(idNguoiHuy);
            ls.setIdMaGiamGia(dp.getIdMaGiamGia());
            ls.setSoTienHoan(hoan);
            ls.setGhiChu(ghiChu);
            luuLichSuHuy(s, ls);
            tx.commit();
            return ls;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            if (e instanceof NghiepVuException) throw (NghiepVuException) e;
            throw new RuntimeException("Lỗi hủy đặt phòng", e);
        }
    }

    BigDecimal tinhTienHoan(DatPhong dp, String loaiHuy) {
        BigDecimal daThu = DatPhong.DA_XAC_NHAN.equals(dp.getTrangThai())
                ? dp.getTienCoc().add(dp.getTienThanhToan())
                : BigDecimal.ZERO;
        if (DatPhong.KHACH_SAN_HUY.equals(loaiHuy)) return daThu;

        long gioConLai = Duration.between(LocalDateTime.now(), dp.getThoiGianNhanPhong()).toHours();
        if (gioConLai >= HOAN_DAY_DU_TRUOC_GIO) return daThu;
        if (gioConLai >= HOAN_MOT_NUA_TRUOC_GIO)
            return daThu.divide(BigDecimal.valueOf(2), 0, RoundingMode.HALF_UP);
        return BigDecimal.ZERO;
    }

    public List<DatPhong> traCuuDatPhong(String maDatPhong, String tuKhoaKhach, String trangThai,
                                         LocalDate tuNgay, LocalDate denNgay) {
        if (tuNgay != null && denNgay != null && denNgay.isBefore(tuNgay))
            throw new NghiepVuException("'Đến ngày' phải sau hoặc bằng 'Từ ngày'.");
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            return timKiem(s, maDatPhong, tuKhoaKhach, trangThai, tuNgay, denNgay);
        }
    }

    public DatPhong layChiTiet(Long id) {
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            DatPhong dp = timTheoIdDayDu(s, id);
            if (dp == null) throw new NghiepVuException("Không tìm thấy đơn đặt phòng.");
            return dp;
        }
    }

    public LichSuDatPhong layLichSuHuy(Long idDatPhong) {
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            return timLichSuHuy(s, idDatPhong);
        }
    }

    private void kiemTraKhoangNgay(LocalDate nhan, LocalDate tra) {
        if (nhan == null || tra == null) throw new NghiepVuException("Vui lòng chọn ngày nhận và ngày trả phòng.");
        if (!tra.isAfter(nhan)) throw new NghiepVuException("Ngày trả phòng phải sau ngày nhận phòng ít nhất 1 ngày.");
        if (nhan.isBefore(LocalDate.now())) throw new NghiepVuException("Ngày nhận phòng không được ở quá khứ.");
    }

    private void kiemTraYeuCau(YeuCauDatPhong yc) {
        if (yc == null || yc.idKhachHang() == null) throw new NghiepVuException("Vui lòng chọn khách hàng.");
        kiemTraKhoangNgay(yc.ngayNhan(), yc.ngayTra());
        if (yc.phongs() == null || yc.phongs().isEmpty()) throw new NghiepVuException("Vui lòng chọn ít nhất 1 phòng.");

        Set<Long> daThay = new HashSet<>();
        for (PhongYeuCau p : yc.phongs()) {
            if (p.idPhong() == null || !daThay.add(p.idPhong()))
                throw new NghiepVuException("Danh sách phòng bị trống hoặc trùng lặp.");
            if (p.soNguoiLon() < 1) throw new NghiepVuException("Mỗi phòng cần ít nhất 1 người lớn.");
            if (p.soTreEmDuoi6() < 0 || p.soTreEmDuoi6() > 2) throw new NghiepVuException("Trẻ dưới 6 tuổi: 0 đến 2 trẻ/phòng.");
            if (p.soTreEmTu6Tuoi() < 0) throw new NghiepVuException("Số trẻ em từ 6 tuổi không hợp lệ.");
        }
    }

    private BigDecimal phanTram(BigDecimal soTien, int phanTram) {
        return soTien.multiply(BigDecimal.valueOf(phanTram)).divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
    }

    private RuntimeException chuyenLoiTrigger(RuntimeException e) {
        for (Throwable t = e; t != null; t = t.getCause()) {
            String m = t.getMessage();
            if (m != null && (m.contains("Phong da duoc dat") || m.contains("So nguoi vuot suc chua")))
                return new NghiepVuException(m.contains("Phong da duoc dat")
                        ? "Phòng vừa được người khác đặt trong khoảng thời gian này. Vui lòng chọn phòng khác."
                        : "Số người vượt quá sức chứa của phòng.");
        }
        return e;
    }
}
