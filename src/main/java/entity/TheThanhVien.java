package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "the_thanh_vien")
public class TheThanhVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_the_thanh_vien")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang", nullable = false, unique = true)
    private KhachHang khachHang;

    @Column(name = "id_hang_thanh_vien", nullable = false)
    private Long idHangThanhVien;

    @Column(name = "ma_qr", nullable = false, unique = true, length = 64)
    private String maQr;

    @Nationalized
    @Column(name = "trang_thai", nullable = false, length = 20)
    private String trangThai = "Hoạt động";

    @Column(name = "so_du", nullable = false, precision = 14, scale = 0)
    private BigDecimal soDu = BigDecimal.ZERO;

    @Column(name = "kich_hoat_luc", nullable = false)
    private LocalDateTime kichHoatLuc;

    @Column(name = "dong_the_luc")
    private LocalDateTime dongTheLuc;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }
    public Long getIdHangThanhVien() { return idHangThanhVien; }
    public void setIdHangThanhVien(Long idHangThanhVien) { this.idHangThanhVien = idHangThanhVien; }
    public String getMaQr() { return maQr; }
    public void setMaQr(String maQr) { this.maQr = maQr; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public BigDecimal getSoDu() { return soDu; }
    public void setSoDu(BigDecimal soDu) { this.soDu = soDu; }
    public LocalDateTime getKichHoatLuc() { return kichHoatLuc; }
    public void setKichHoatLuc(LocalDateTime kichHoatLuc) { this.kichHoatLuc = kichHoatLuc; }
    public LocalDateTime getDongTheLuc() { return dongTheLuc; }
    public void setDongTheLuc(LocalDateTime dongTheLuc) { this.dongTheLuc = dongTheLuc; }
}
