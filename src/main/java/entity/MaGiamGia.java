package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ma_giam_gia")
public class MaGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ma_giam_gia")
    private Long id;

    @Column(name = "ma_giam_gia", nullable = false, unique = true, length = 30)
    private String maGiamGia;

    @Nationalized
    @Column(name = "loai_giam_gia", nullable = false, length = 20)
    private String loaiGiamGia;

    @Column(name = "gia_tri", nullable = false, precision = 14, scale = 2)
    private BigDecimal giaTri;

    @Column(name = "bat_dau_luc", nullable = false)
    private LocalDateTime batDauLuc;

    @Column(name = "ket_thuc_luc", nullable = false)
    private LocalDateTime ketThucLuc;

    @Column(name = "so_luot_da_dung", nullable = false)
    private Integer soLuotDaDung = 0;

    @Nationalized
    @Column(name = "trang_thai", nullable = false, length = 20)
    private String trangThai = "Hoạt động";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaGiamGia() { return maGiamGia; }
    public void setMaGiamGia(String maGiamGia) { this.maGiamGia = maGiamGia; }
    public String getLoaiGiamGia() { return loaiGiamGia; }
    public void setLoaiGiamGia(String loaiGiamGia) { this.loaiGiamGia = loaiGiamGia; }
    public BigDecimal getGiaTri() { return giaTri; }
    public void setGiaTri(BigDecimal giaTri) { this.giaTri = giaTri; }
    public LocalDateTime getBatDauLuc() { return batDauLuc; }
    public void setBatDauLuc(LocalDateTime batDauLuc) { this.batDauLuc = batDauLuc; }
    public LocalDateTime getKetThucLuc() { return ketThucLuc; }
    public void setKetThucLuc(LocalDateTime ketThucLuc) { this.ketThucLuc = ketThucLuc; }
    public Integer getSoLuotDaDung() { return soLuotDaDung; }
    public void setSoLuotDaDung(Integer soLuotDaDung) { this.soLuotDaDung = soLuotDaDung; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
