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

    @Column(name = "id_nhan_vien")
    private Long idNhanVien;

    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;

    @Column(name = "mat_khau", length = 255, nullable = false)
    private String matKhau;

    @Column(name = "id_vai_tro", nullable = false)
    private Long idVaiTro;

    @Column(name = "trang_thai", length = 20, nullable = false)
    private String trangThai; // HOAT_DONG, BI_KHOA

    public Long getIdNguoiDung() { return idNguoiDung; }
    public void setIdNguoiDung(Long idNguoiDung) { this.idNguoiDung = idNguoiDung; }

    public Long getIdNhanVien() { return idNhanVien; }
    public void setIdNhanVien(Long idNhanVien) { this.idNhanVien = idNhanVien; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public Long getIdVaiTro() { return idVaiTro; }
    public void setIdVaiTro(Long idVaiTro) { this.idVaiTro = idVaiTro; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
