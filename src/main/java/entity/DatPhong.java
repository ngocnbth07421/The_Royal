package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "dat_phong")
public class DatPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dat_phong")
    private Integer idDatPhong;

    @Column(name = "id_khach_hang")
    private Integer idKhachHang;

    @Column(name = "id_nguoi_tao")
    private Integer idNguoiTao;

    @Column(name = "id_ma_giam_gia")
    private Integer idMaGiamGia;

    @Column(name = "ma_dat_phong")
    private String maDatPhong;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "thoi_gian_nhan_phong")
    private LocalDateTime thoiGianNhanPhong;

    @Column(name = "thoi_gian_tra_phong")
    private LocalDateTime thoiGianTraPhong;

    @Column(name = "so_dem")
    private Integer soDem;

    @Column(name = "tien_phong")
    private BigDecimal tienPhong;

    @Column(name = "giam_gia")
    private BigDecimal giamGia;

    @Column(name = "phi_dich_vu")
    private BigDecimal phiDichVu;

    @Column(name = "thue_vat")
    private BigDecimal thueVat;

    @Column(name = "tong_tien")
    private BigDecimal tongTien;

    @Column(name = "tien_coc")
    private BigDecimal tienCoc;

    @Column(name = "tien_thanh_toan")
    private BigDecimal tienThanhToan;
}
