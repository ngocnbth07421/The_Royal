package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nhan_vien")
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nhan_vien")
    private Long idNhanVien;

    @Column(name = "id_vai_tro", nullable = false)
    private Long idVaiTro;

    @Column(name = "ma_nhan_vien", length = 20, nullable = false, unique = true)
    private String maNhanVien;

    @Column(name = "ho_ten", length = 100, nullable = false)
    private String hoTen;

    @Column(name = "so_dien_thoai", length = 15)
    private String soDienThoai;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_vao_lam")
    private Date ngayVaoLam;

    @Column(name = "luong_co_ban")
    private BigDecimal luongCoBan;

    @Column(name = "phu_cap")
    private BigDecimal phuCap;

    @Column(name = "khau_tru")
    private BigDecimal khauTru;

    @Column(name = "trang_thai", length = 15, nullable = false)
    private String trangThai;

    public Long getIdNhanVien() { return idNhanVien; }
    public void setIdNhanVien(Long idNhanVien) { this.idNhanVien = idNhanVien; }

    public Long getIdVaiTro() { return idVaiTro; }
    public void setIdVaiTro(Long idVaiTro) { this.idVaiTro = idVaiTro; }

    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public Date getNgayVaoLam() { return ngayVaoLam; }
    public void setNgayVaoLam(Date ngayVaoLam) { this.ngayVaoLam = ngayVaoLam; }

    public BigDecimal getLuongCoBan() { return luongCoBan; }
    public void setLuongCoBan(BigDecimal luongCoBan) { this.luongCoBan = luongCoBan; }

    public BigDecimal getPhuCap() { return phuCap; }
    public void setPhuCap(BigDecimal phuCap) { this.phuCap = phuCap; }

    public BigDecimal getKhauTru() { return khauTru; }
    public void setKhauTru(BigDecimal khauTru) { this.khauTru = khauTru; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
