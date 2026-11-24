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

    // Quan hệ: 1 nhân viên - 1 người dùng (tài khoản hệ thống)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung", referencedColumnName = "id_nguoi_dung", unique = true)
    private NguoiDung nguoiDung;

    @Column(name = "id_nguoi_dung", insertable = false, updatable = false)
    private Long idNguoiDung;

    // Quan hệ: Nhiều nhân viên - 1 vai trò
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vai_tro", referencedColumnName = "id_vai_tro")
    private VaiTro vaiTro;

    @Column(name = "id_vai_tro", insertable = false, updatable = false)
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

    public NguoiDung getNguoiDung() { return nguoiDung; }
    public void setNguoiDung(NguoiDung nguoiDung) { this.nguoiDung = nguoiDung; }

    public Long getIdNguoiDung() { return nguoiDung != null ? nguoiDung.getIdNguoiDung() : idNguoiDung; }
    public void setIdNguoiDung(Long idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
        if (idNguoiDung != null) {
            if (this.nguoiDung == null) this.nguoiDung = new NguoiDung();
            this.nguoiDung.setIdNguoiDung(idNguoiDung);
        } else {
            this.nguoiDung = null;
        }
    }

    public Long getIdNhanVien() { return idNhanVien; }
    public void setIdNhanVien(Long idNhanVien) { this.idNhanVien = idNhanVien; }

    public VaiTro getVaiTro() { return vaiTro; }
    public void setVaiTro(VaiTro vaiTro) { this.vaiTro = vaiTro; }

    public Long getIdVaiTro() { return vaiTro != null ? vaiTro.getIdVaiTro() : null; }
    public void setIdVaiTro(Long idVaiTro) {
        if (idVaiTro != null) {
            if (this.vaiTro == null) this.vaiTro = new VaiTro();
            this.vaiTro.setIdVaiTro(idVaiTro);
        } else {
            this.vaiTro = null;
        }
    }

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

    // Trạng thái lấy từ bảng người dùng (quan hệ 1-1)
    public String getTrangThai() {
        return nguoiDung != null ? nguoiDung.getTrangThai() : "Hoạt động";
    }
    public void setTrangThai(String trangThai) {
        if (this.nguoiDung != null) {
            this.nguoiDung.setTrangThai(trangThai);
        }
    }
}
