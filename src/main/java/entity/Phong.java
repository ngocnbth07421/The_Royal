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
@Table(name = "phong")
public class Phong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phong")
    private Long idPhong;

    @Column(name = "id_tang", nullable = false)
    private Long idTang;

    @Column(name = "id_loai_phong", nullable = false)
    private Long idLoaiPhong;

    @Column(name = "ma_phong", length = 10, nullable = false, unique = true)
    private String maPhong;

    @Column(name = "hinh_anh_phong", length = 500)
    private String hinhAnhPhong;

    @Column(name = "trang_thai", length = 25, nullable = false)
    private String trangThai; // SAN_SANG, GIU_CHO, DANG_O, CAN_DON, BAO_TRI

    @Column(name = "ghi_chu", length = 255)
    private String ghiChu; // View phòng: Ocean View, City View, Mountain View

    public Long getIdPhong() { return idPhong; }
    public void setIdPhong(Long idPhong) { this.idPhong = idPhong; }

    public Long getIdTang() { return idTang; }
    public void setIdTang(Long idTang) { this.idTang = idTang; }

    public Long getIdLoaiPhong() { return idLoaiPhong; }
    public void setIdLoaiPhong(Long idLoaiPhong) { this.idLoaiPhong = idLoaiPhong; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public String getHinhAnhPhong() { return hinhAnhPhong; }
    public void setHinhAnhPhong(String hinhAnhPhong) { this.hinhAnhPhong = hinhAnhPhong; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
