package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "hang_phong")
public class HangPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hang_phong")
    private Long id;

    @Column(name = "ma_hang_phong", nullable = false, length = 20, unique = true)
    private String maHangPhong;

    @Nationalized
    @Column(name = "mo_ta", length = 255)
    private String moTa;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaHangPhong() { return maHangPhong; }
    public void setMaHangPhong(String maHangPhong) { this.maHangPhong = maHangPhong; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}
