package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.util.HashSet;
import java.util.Set;

/**
 * Bảng phong. LƯU Ý: file này thay cho class Phong "khung" trên main -> thống nhất với bạn làm trang chủ
 * (nếu họ đã thêm field gì thì giữ lại, chỉ cần đủ các cột dưới đây).
 */
@Entity
@Table(name = "phong")
public class Phong {
    public static final String SAN_SANG = "SAN_SANG";
    public static final String DANG_O   = "DANG_O";
    public static final String CAN_DON  = "CAN_DON";
    public static final String BAO_TRI  = "BAO_TRI";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_phong")
    private Long id;

    // Quan hệ: Nhiều phòng - 1 tầng
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tang", referencedColumnName = "id_tang", nullable = false)
    private Tang tang;

    @Column(name = "id_tang", insertable = false, updatable = false)
    private Long idTang;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_loai_phong", nullable = false)
    private LoaiPhong loaiPhong;

    @Column(name = "ma_phong", nullable = false, length = 10, unique = true)
    private String maPhong;

    @Column(name = "hinh_anh_phong", length = 500)
    private String hinhAnhPhong;

    @Column(name = "trang_thai", nullable = false, length = 15)
    private String trangThai = SAN_SANG;

    @Nationalized
    @Column(name = "ghi_chu", length = 255)
    private String ghiChu;

    /** Bảng nối tien_nghi_phong (PK kép id_phong + id_tien_nghi). */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tien_nghi_phong",
            joinColumns = @JoinColumn(name = "id_phong"),
            inverseJoinColumns = @JoinColumn(name = "id_tien_nghi"))
    private Set<TienNghi> tienNghis = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Tang getTang() { return tang; }
    public void setTang(Tang tang) { this.tang = tang; }
    public Long getIdTang() { return tang != null ? tang.getIdTang() : null; }
    public void setIdTang(Long idTang) {
        if (idTang != null) {
            if (this.tang == null) this.tang = new Tang();
            this.tang.setIdTang(idTang);
        } else {
            this.tang = null;
        }
    }
    public LoaiPhong getLoaiPhong() { return loaiPhong; }
    public void setLoaiPhong(LoaiPhong loaiPhong) { this.loaiPhong = loaiPhong; }
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public String getHinhAnhPhong() { return hinhAnhPhong; }
    public void setHinhAnhPhong(String hinhAnhPhong) { this.hinhAnhPhong = hinhAnhPhong; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    public Set<TienNghi> getTienNghis() { return tienNghis; }
    public void setTienNghis(Set<TienNghi> tienNghis) { this.tienNghis = tienNghis; }

    // Compatibility methods for HomeServlet and PhongServlet
    public Long getIdLoaiPhong() {
        return loaiPhong != null ? loaiPhong.getId() : null;
    }
    public void setIdLoaiPhong(Long idLoaiPhong) {
        if (idLoaiPhong != null) {
            if (this.loaiPhong == null) {
                this.loaiPhong = new LoaiPhong();
            }
            this.loaiPhong.setId(idLoaiPhong);
        } else {
            this.loaiPhong = null;
        }
    }
}
