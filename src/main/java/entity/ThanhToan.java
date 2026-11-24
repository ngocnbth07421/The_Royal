package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Entity
@Table(name = "thanh_toan")
public class ThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thanh_toan")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dat_phong", nullable = false)
    private DatPhong datPhong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_the_thanh_vien")
    private TheThanhVien theThanhVien;

    @Nationalized
    @Column(name = "phuong_thuc_thanh_toan", nullable = false, length = 20)
    private String phuongThucThanhToan;

    @Column(name = "tien_thanh_toan", nullable = false, precision = 14, scale = 0)
    private BigDecimal tienThanhToan;

    @Nationalized
    @Column(name = "trang_thai", nullable = false, length = 25)
    private String trangThai = "Chờ xử lý";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DatPhong getDatPhong() { return datPhong; }
    public void setDatPhong(DatPhong datPhong) { this.datPhong = datPhong; }
    public TheThanhVien getTheThanhVien() { return theThanhVien; }
    public void setTheThanhVien(TheThanhVien theThanhVien) { this.theThanhVien = theThanhVien; }
    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(String phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }
    public BigDecimal getTienThanhToan() { return tienThanhToan; }
    public void setTienThanhToan(BigDecimal tienThanhToan) { this.tienThanhToan = tienThanhToan; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
