package entity;

import java.time.LocalDate;
import java.util.List;

/**
 * Dữ liệu đầu vào của nghiệp vụ "Đặt phòng" (Servlet gom từ form -> đưa cho Service).
 * idNguoiTao = null nghĩa là khách tự đặt; maGiamGia có thể null/rỗng.
 */
public record YeuCauDatPhong(Long idKhachHang,
                             Long idNguoiTao,
                             String maGiamGia,
                             LocalDate ngayNhan,
                             LocalDate ngayTra,
                             List<PhongYeuCau> phongs) {}
