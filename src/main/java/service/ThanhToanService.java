package service;

import entity.DatPhong;
import entity.ThanhToan;
import entity.TheThanhVien;
import exception.NghiepVuException;
import repository.ThanhToanRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Tầng xử lý nghiệp vụ Thanh toán đơn đặt phòng.
 */
public class ThanhToanService {
    private final ThanhToanRepository repo = new ThanhToanRepository();

    public List<ThanhToan> layDanhSach() { return repo.findAll(); }
    public List<DatPhong> layDanhSachDatPhong() { return repo.findAllDatPhong(); }
    public List<TheThanhVien> layDanhSachTheThanhVien() { return repo.findAllTheThanhVienHoatDong(); }

    /**
     * Nghiệp vụ: Kiểm tra ràng buộc thanh toán:
     * 1. Số tiền thanh toán > 0 (khớp CHECK (tien_thanh_toan > 0)).
     * 2. Phương thức chỉ nhận 'TIEN_MAT', 'VNPAY', 'THE'.
     * 3. Nếu phương thức là 'THE', bắt buộc phải có id_the_thanh_vien (khớp CHECK của bảng thanh_toan).
     */
    public void taoGiaoDichThanhToan(Long idDatPhong, Long idTheThanhVien, String phuongThuc, BigDecimal soTien, String trangThai) {
        if (idDatPhong == null) {
            throw new NghiepVuException("Vui lòng chọn đơn đặt phòng cần thanh toán.");
        }
        if (soTien == null || soTien.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NghiepVuException("Số tiền thanh toán phải lớn hơn 0.");
        }
        if ("THE".equals(phuongThuc) && idTheThanhVien == null) {
            throw new NghiepVuException("Khi chọn phương thức Thẻ thành viên (THE), bắt buộc phải chọn thẻ của khách!");
        }

        DatPhong dp = new DatPhong();
        dp.setId(idDatPhong);

        ThanhToan tt = new ThanhToan();
        tt.setDatPhong(dp);

        if ("THE".equals(phuongThuc) && idTheThanhVien != null) {
            TheThanhVien ttv = new TheThanhVien();
            ttv.setId(idTheThanhVien);
            tt.setTheThanhVien(ttv);
        } else {
            tt.setTheThanhVien(null);
        }

        tt.setPhuongThucThanhToan(phuongThuc);
        tt.setTienThanhToan(soTien);
        tt.setTrangThai(trangThai != null ? trangThai : "THANH_CONG");

        repo.saveOrUpdate(tt);
    }

    /**
     * Nghiệp vụ: Cập nhật trạng thái phiếu thu (Duyệt thanh toán hoặc Hoàn tiền).
     */
    public void doiTrangThai(Long idThanhToan, String trangThaiMoi) {
        repo.capNhatTrangThai(idThanhToan, trangThaiMoi);
    }
}