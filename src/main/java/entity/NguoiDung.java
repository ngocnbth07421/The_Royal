package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nguoi_dung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nguoi_dung")
    private Long idNguoiDung;

    // Quan hệ: Nhiều người dùng - 1 nhân viên (tùy chọn)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_nhan_vien", referencedColumnName = "id_nhan_vien")
    private NhanVien nhanVien;

    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;

    @Column(name = "mat_khau", length = 255, nullable = false)
    private String matKhau;

    // Quan hệ: Nhiều người dùng - 1 vai trò
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vai_tro", referencedColumnName = "id_vai_tro", nullable = false)
    private VaiTro vaiTro;

    @Column(name = "trang_thai", length = 20, nullable = false)
    private String trangThai; // HOAT_DONG, BI_KHOA

    public Long getIdNguoiDung() { return idNguoiDung; }
    public void setIdNguoiDung(Long idNguoiDung) { this.idNguoiDung = idNguoiDung; }

    public NhanVien getNhanVien() { return nhanVien; }
    public void setNhanVien(NhanVien nhanVien) { this.nhanVien = nhanVien; }

    public Long getIdNhanVien() { return nhanVien != null ? nhanVien.getIdNhanVien() : null; }
    public void setIdNhanVien(Long idNhanVien) {
        if (idNhanVien != null) {
            if (this.nhanVien == null) this.nhanVien = new NhanVien();
            this.nhanVien.setIdNhanVien(idNhanVien);
        } else {
            this.nhanVien = null;
        }
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

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

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
