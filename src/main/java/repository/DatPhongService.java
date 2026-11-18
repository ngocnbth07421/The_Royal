package repository;

import entity.*;
import util.NghiepVuException;
import org.hibernate.Session;
import util.TransactionHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/** Nghiệp vụ "Quản lý đặt phòng": Tra cứu phòng, Đặt phòng, Hủy phòng, Tra cứu đặt phòng. */
public class DatPhongService {

    // ===== CÁC THAM SỐ NGHIỆP VỤ - cần cả nhóm chốt lại (xem giải thích) =====
    static final BigDecimal TY_LE_VAT = new BigDecimal("0.10");   // seed data không nhất quán (8-10%), tạm dùng 10%
    static final BigDecimal TY_LE_COC = new BigDecimal("0.30");   // cọc = 30% tổng tiền
    static final LocalTime  GIO_NHAN_PHONG = LocalTime.of(14, 0); // theo seed: nhận 14:00
    static final LocalTime  GIO_TRA_PHONG  = LocalTime.of(12, 0); // theo seed: trả 12:00
    static final int HOAN_DAY_DU_TRUOC_GIO = 48;                  // khách hủy trước >= 48h: hoàn 100% cọc
    static final int HOAN_MOT_NUA_TRUOC_GIO = 24;                 // 24h-48h: hoàn 50%; < 24h: không hoàn

    private final DatPhongRepository repo = new DatPhongRepository();

    // ================================================================ 1. TRA CỨU PHÒNG
    public List<Phong> traCuuPhongTrong(LocalDate nhan, LocalDate tra, Long idHangPhong,
                                        Long idLoaiGiuong, int soNguoi) {
        kiemTraKhoangNgay(nhan, tra);
        if (soNguoi < 1) throw new NghiepVuException("Số người phải >= 1.");
        return TransactionHelper.chay(s -> {
            List<Phong> ds = repo.timPhongTrong(s, nhan, tra, idHangPhong, idLoaiGiuong, soNguoi);
            return ds;
        });
    }

    public List<HangPhong> layHangPhong()   { return TransactionHelper.chay(repo::layHangPhong); }
    public List<LoaiGiuong> layLoaiGiuong() { return TransactionHelper.chay(repo::layLoaiGiuong); }
    public List<KhachHang> timKhachHang(String tuKhoa) { return TransactionHelper.chay(s -> repo.timKhachHang(s, tuKhoa)); }
    public List<Phong> layPhongDeDat(List<Long> ids)   { return TransactionHelper.chay(s -> repo.timPhongTheoDanhSachId(s, ids)); }

    // ================================================================ 2. ĐẶT PHÒNG
    public DatPhong datPhong(YeuCauDatPhong yc) {
        kiemTraYeuCau(yc);
        try {
            return TransactionHelper.chay(s -> thucHienDatPhong(s, yc));
        } catch (NghiepVuException e) {
            throw e;
        } catch (RuntimeException e) {
            throw chuyenLoiTrigger(e);   // trigger DB báo trùng phòng / vượt sức chứa -> thông báo thân thiện
        }
    }

    private DatPhong thucHienDatPhong(Session s, YeuCauDatPhong yc) {
        KhachHang kh = s.find(KhachHang.class, yc.idKhachHang());
        if (kh == null || !"HOAT_DONG".equals(kh.getTrangThai()))
            throw new NghiepVuException("Khách hàng không tồn tại hoặc đang bị khóa.");

        int soDem = (int) ChronoUnit.DAYS.between(yc.ngayNhan(), yc.ngayTra());

        DatPhong dp = new DatPhong();
        dp.setKhachHang(kh);
        dp.setIdNguoiTao(yc.idNguoiTao());
        dp.setThoiGianNhanPhong(yc.ngayNhan().atTime(GIO_NHAN_PHONG));
        dp.setThoiGianTraPhong(yc.ngayTra().atTime(GIO_TRA_PHONG));
        dp.setSoDem(soDem);

        // --- Duyệt từng phòng theo thứ tự id tăng dần (khóa theo thứ tự cố định -> tránh deadlock)
        BigDecimal tienPhong = BigDecimal.ZERO;
        List<PhongYeuCau> dsPhong = yc.phongs().stream()
                .sorted(Comparator.comparing(PhongYeuCau::idPhong)).toList();
        for (PhongYeuCau pyc : dsPhong) {
            Phong p = repo.timPhongVaKhoa(s, pyc.idPhong());
            if (p == null) throw new NghiepVuException("Phòng id=" + pyc.idPhong() + " không tồn tại.");
            if (Phong.BAO_TRI.equals(p.getTrangThai()))
                throw new NghiepVuException("Phòng " + p.getMaPhong() + " đang bảo trì.");

            int sucChua = p.getLoaiPhong().getLoaiGiuong().getSucChua();
            if (pyc.soNguoiLon() + pyc.soTreEmTu6Tuoi() > sucChua)
                throw new NghiepVuException("Phòng " + p.getMaPhong() + " chỉ chứa tối đa " + sucChua
                        + " người (người lớn + trẻ từ 6 tuổi).");
            if (repo.phongBiTrung(s, p.getId(), yc.ngayNhan(), yc.ngayTra()))
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

        // --- Giảm giá = % theo hạng thẻ thành viên + mã giảm giá (nếu có), không vượt quá tiền phòng
        BigDecimal giamGia = phanTram(tienPhong, repo.phanTramGiamHangThanhVien(s, kh.getId()));
        if (yc.maGiamGia() != null && !yc.maGiamGia().isBlank()) {
            Object[] mg = repo.timMaGiamGiaHopLe(s, yc.maGiamGia().trim(), LocalDateTime.now());
            if (mg == null) throw new NghiepVuException("Mã giảm giá không tồn tại, đã hết hạn hoặc đã ngừng.");
            Long idMa = ((Number) mg[0]).longValue();
            BigDecimal giaTri = new BigDecimal(mg[2].toString());
            BigDecimal giamTuMa = "PHAN_TRAM".equals(mg[1].toString())
                    ? tienPhong.multiply(giaTri).divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP)
                    : giaTri.setScale(0, RoundingMode.HALF_UP);
            giamGia = giamGia.add(giamTuMa);
            dp.setIdMaGiamGia(idMa);
            repo.tangLuotDungMaGiamGia(s, idMa);
        }
        giamGia = giamGia.min(tienPhong);

        // --- Tính tiền
        BigDecimal phiDichVu = BigDecimal.ZERO;   // dịch vụ phát sinh trong lúc ở -> cộng sau (module Dịch vụ)
        BigDecimal truocThue = tienPhong.subtract(giamGia).add(phiDichVu);
        BigDecimal thue = truocThue.multiply(TY_LE_VAT).setScale(0, RoundingMode.HALF_UP);
        BigDecimal tong = truocThue.add(thue);

        dp.setMaDatPhong(repo.sinhMaDatPhong(s));
        dp.setTrangThai(DatPhong.CHO_COC);
        dp.setTienPhong(tienPhong);
        dp.setGiamGia(giamGia);
        dp.setPhiDichVu(phiDichVu);
        dp.setThueVat(thue);
        dp.setTongTien(tong);
        dp.setTienCoc(tong.multiply(TY_LE_COC).setScale(0, RoundingMode.HALF_UP));

        repo.luuDatPhong(s, dp);   // cascade lưu luôn các dat_phong_chi_tiet
        return dp;
    }

    // ================================================================ 3. HỦY PHÒNG
    /**
     * @param loaiHuy KHACH_HUY hoặc KHACH_SAN_HUY
     * @param idNguoiHuy id nhân viên thực hiện (null nếu khách tự hủy)
     */
    public LichSuDatPhong huyDatPhong(Long idDatPhong, String loaiHuy, Long idNguoiHuy, String ghiChu) {
        if (!DatPhong.KHACH_HUY.equals(loaiHuy) && !DatPhong.KHACH_SAN_HUY.equals(loaiHuy))
            throw new NghiepVuException("Loại hủy không hợp lệ.");
        if (ghiChu != null && ghiChu.length() > 255) throw new NghiepVuException("Ghi chú tối đa 255 ký tự.");

        return TransactionHelper.chay(s -> {
            DatPhong dp = repo.timVaKhoaDatPhong(s, idDatPhong);   // khóa để tránh 2 người hủy cùng lúc
            if (dp == null) throw new NghiepVuException("Không tìm thấy đơn đặt phòng.");
            if (!DatPhong.CHO_COC.equals(dp.getTrangThai()) && !DatPhong.DA_XAC_NHAN.equals(dp.getTrangThai()))
                throw new NghiepVuException("Chỉ hủy được đơn đang 'Chờ cọc' hoặc 'Đã xác nhận' (hiện tại: "
                        + dp.getTrangThai() + ").");

            BigDecimal hoan = tinhTienHoan(dp, loaiHuy);

            dp.setTrangThai(loaiHuy);
            repo.danhDauHuyChiTiet(s, dp.getId());   // nhả phòng: trạng thái DA_HUY -> không còn bị tính là chiếm phòng

            LichSuDatPhong ls = new LichSuDatPhong();
            ls.setIdDatPhong(dp.getId());
            ls.setIdNguoiHuy(idNguoiHuy);
            ls.setIdMaGiamGia(dp.getIdMaGiamGia());
            ls.setSoTienHoan(hoan);
            ls.setGhiChu(ghiChu);
            repo.luuLichSuHuy(s, ls);
            return ls;
        });
    }

    /** Chính sách hoàn tiền (tạm, cần nhóm chốt): chỉ hoàn phần ĐÃ thu (cọc khi đơn đã xác nhận). */
    BigDecimal tinhTienHoan(DatPhong dp, String loaiHuy) {
        BigDecimal daThu = DatPhong.DA_XAC_NHAN.equals(dp.getTrangThai())
                ? dp.getTienCoc().add(dp.getTienThanhToan())
                : BigDecimal.ZERO;                               // CHO_COC: chưa thu đồng nào
        if (DatPhong.KHACH_SAN_HUY.equals(loaiHuy)) return daThu; // khách sạn hủy: hoàn 100%

        long gioConLai = Duration.between(LocalDateTime.now(), dp.getThoiGianNhanPhong()).toHours();
        if (gioConLai >= HOAN_DAY_DU_TRUOC_GIO) return daThu;
        if (gioConLai >= HOAN_MOT_NUA_TRUOC_GIO)
            return daThu.divide(BigDecimal.valueOf(2), 0, RoundingMode.HALF_UP);
        return BigDecimal.ZERO;
    }

    // ================================================================ 4. TRA CỨU ĐẶT PHÒNG
    public List<DatPhong> traCuuDatPhong(String maDatPhong, String tuKhoaKhach, String trangThai,
                                         LocalDate tuNgay, LocalDate denNgay) {
        if (tuNgay != null && denNgay != null && denNgay.isBefore(tuNgay))
            throw new NghiepVuException("'Đến ngày' phải sau hoặc bằng 'Từ ngày'.");
        return TransactionHelper.chay(s -> repo.timKiem(s, maDatPhong, tuKhoaKhach, trangThai, tuNgay, denNgay));
    }

    public DatPhong layChiTiet(Long id) {
        DatPhong dp = TransactionHelper.chay(s -> repo.timTheoIdDayDu(s, id));
        if (dp == null) throw new NghiepVuException("Không tìm thấy đơn đặt phòng.");
        return dp;
    }

    public LichSuDatPhong layLichSuHuy(Long idDatPhong) {
        return TransactionHelper.chay(s -> repo.timLichSuHuy(s, idDatPhong));
    }

    // ================================================================ 5. LỊCH ĐẶT PHÒNG
    /** Lượt ở giao với [tu, den) để vẽ lịch. Giới hạn 130 ngày/lần để không tải quá nhiều dữ liệu. */
    public List<DatPhongChiTiet> layLich(LocalDate tu, LocalDate den, String maPhong,
                                         String trangThaiDon, boolean goiHuy) {
        if (tu == null || den == null || !den.isAfter(tu)) throw new NghiepVuException("Khoảng ngày của lịch không hợp lệ.");
        if (tu.plusDays(130).isBefore(den)) throw new NghiepVuException("Khoảng ngày của lịch quá dài.");
        String ma = (maPhong == null || maPhong.isBlank()) ? null : maPhong.trim();
        String tt = (trangThaiDon == null || trangThaiDon.isBlank()) ? null : trangThaiDon.trim();
        return TransactionHelper.chay(s -> repo.timLichTheoKhoang(s, tu, den, ma, tt, goiHuy));
    }

    // ================================================================ HÀM PHỤ
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

    /** Trigger SQL Server RAISERROR -> lấy message gốc ra làm NghiepVuException. */
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
