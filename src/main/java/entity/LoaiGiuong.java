package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "loai_giuong")
public class LoaiGiuong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_loai_giuong")
    private Long id;

    @Column(name = "ma_loai_giuong", nullable = false, length = 20, unique = true)
    private String maLoaiGiuong;

    @Nationalized
    @Column(name = "ten", nullable = false, length = 100)
    private String ten;

    @Column(name = "suc_chua", nullable = false)
    private Integer sucChua;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaLoaiGiuong() { return maLoaiGiuong; }
    public void setMaLoaiGiuong(String maLoaiGiuong) { this.maLoaiGiuong = maLoaiGiuong; }
    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }
    public Integer getSucChua() { return sucChua; }
    public void setSucChua(Integer sucChua) { this.sucChua = sucChua; }
}
