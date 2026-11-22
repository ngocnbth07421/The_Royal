package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

/** Bảng lich_su_dat_phong = "biên bản hủy" của 1 đơn đặt phòng (id_dat_phong UNIQUE -> mỗi đơn hủy tối đa 1 lần). */
@Entity
@Table(name = "lich_su_dat_phong")
public class LichSuDatPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lich_su_dat_phong")
    private Long id;

    @Column(name = "id_dat_phong", nullable = false, unique = true)
    private Long idDatPhong;

    @Column(name = "id_nguoi_huy")
    private Long idNguoiHuy;

    @Column(name = "id_ma_giam_gia")
    private Long idMaGiamGia;

    @Column(name = "so_tien_hoan", nullable = false, precision = 14, scale = 0)
    private BigDecimal soTienHoan = BigDecimal.ZERO;

    @Nationalized
    @Column(name = "ghi_chu", length = 255)
    private String ghiChu;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdDatPhong() { return idDatPhong; }
    public void setIdDatPhong(Long idDatPhong) { this.idDatPhong = idDatPhong; }
    public Long getIdNguoiHuy() { return idNguoiHuy; }
    public void setIdNguoiHuy(Long idNguoiHuy) { this.idNguoiHuy = idNguoiHuy; }
    public Long getIdMaGiamGia() { return idMaGiamGia; }
    public void setIdMaGiamGia(Long idMaGiamGia) { this.idMaGiamGia = idMaGiamGia; }
    public BigDecimal getSoTienHoan() { return soTienHoan; }
    public void setSoTienHoan(BigDecimal soTienHoan) { this.soTienHoan = soTienHoan; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
