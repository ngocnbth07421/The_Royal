package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loai_phong")
public class LoaiPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_loai_phong")
    private Long idLoaiPhong;

    @Column(name = "id_hang_phong", nullable = false)
    private Long idHangPhong;

    @Column(name = "id_loai_giuong", nullable = false)
    private Long idLoaiGiuong;

    @Column(name = "ten", length = 150, nullable = false)
    private String ten;

    @Column(name = "gia_moi_dem", nullable = false)
    private BigDecimal giaMoiDem;

    @Column(name = "mo_ta")
    private String moTa;

    public Long getIdLoaiPhong() { return idLoaiPhong; }
    public void setIdLoaiPhong(Long idLoaiPhong) { this.idLoaiPhong = idLoaiPhong; }

    public Long getIdHangPhong() { return idHangPhong; }
    public void setIdHangPhong(Long idHangPhong) { this.idHangPhong = idHangPhong; }

    public Long getIdLoaiGiuong() { return idLoaiGiuong; }
    public void setIdLoaiGiuong(Long idLoaiGiuong) { this.idLoaiGiuong = idLoaiGiuong; }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }

    public BigDecimal getGiaMoiDem() { return giaMoiDem; }
    public void setGiaMoiDem(BigDecimal giaMoiDem) { this.giaMoiDem = giaMoiDem; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}
