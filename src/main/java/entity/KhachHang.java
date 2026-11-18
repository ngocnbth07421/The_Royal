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
@Table(name = "khach_hang")
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_khach_hang")
    private Long id_khach_hang;

    @Column(name = "ho_ten", length = 150, nullable = false)
    private String ho_ten;

    @Column(name = "so_dien_thoai", length = 20)
    private String so_dien_thoai;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "trang_thai", length = 20, nullable = false)
    private String trang_thai;

    // Standard CamelCase getters/setters
    public Long getIdKhachHang() { return id_khach_hang; }
    public void setIdKhachHang(Long idKhachHang) { this.id_khach_hang = idKhachHang; }

    public String getHoTen() { return ho_ten; }
    public void setHoTen(String hoTen) { this.ho_ten = hoTen; }

    public String getSoDienThoai() { return so_dien_thoai; }
    public void setSoDienThoai(String soDienThoai) { this.so_dien_thoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTrangThai() { return trang_thai; }
    public void setTrangThai(String trangThai) { this.trang_thai = trangThai; }

    // Snake_case getters/setters for backward compatibility
    public Long getId_khach_hang() { return id_khach_hang; }
    public void setId_khach_hang(Long id_khach_hang) { this.id_khach_hang = id_khach_hang; }

    public String getHo_ten() { return ho_ten; }
    public void setHo_ten(String ho_ten) { this.ho_ten = ho_ten; }

    public String getSo_dien_thoai() { return so_dien_thoai; }
    public void setSo_dien_thoai(String so_dien_thoai) { this.so_dien_thoai = so_dien_thoai; }

    public String getTrang_thai() { return trang_thai; }
    public void setTrang_thai(String trang_thai) { this.trang_thai = trang_thai; }
}
