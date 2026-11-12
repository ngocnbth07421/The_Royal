package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Entity
@Table(name = "loai_phong")
public class LoaiPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_loai_phong")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_hang_phong", nullable = false)
    private HangPhong hangPhong;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_loai_giuong", nullable = false)
    private LoaiGiuong loaiGiuong;

    @Nationalized
    @Column(name = "ten", nullable = false, length = 150)
    private String ten;

    @Column(name = "gia_moi_dem", nullable = false, precision = 14, scale = 0)
    private BigDecimal giaMoiDem;

    @Nationalized
    @Column(name = "mo_ta", length = 4000)
    private String moTa;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public HangPhong getHangPhong() { return hangPhong; }
    public void setHangPhong(HangPhong hangPhong) { this.hangPhong = hangPhong; }
    public LoaiGiuong getLoaiGiuong() { return loaiGiuong; }
    public void setLoaiGiuong(LoaiGiuong loaiGiuong) { this.loaiGiuong = loaiGiuong; }
    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }
    public BigDecimal getGiaMoiDem() { return giaMoiDem; }
    public void setGiaMoiDem(BigDecimal giaMoiDem) { this.giaMoiDem = giaMoiDem; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}
