package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

/** Bảng lich_su_dat_phong = "biên bản hủy" của 1 đơn đặt phòng (id_dat_phong UNIQUE -> mỗi đơn hủy tối đa 1 lần). */
@Entity
@Table(name = "lich_su_dat_phong")
public class LichSuDatPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lich_su_dat_phong")
    private Long id;

    // Quan hệ: 1 biên bản hủy - 1 đặt phòng (UNIQUE)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dat_phong", referencedColumnName = "id_dat_phong", nullable = false, unique = true)
    private DatPhong datPhong;

    @Column(name = "id_dat_phong", insertable = false, updatable = false)
    private Long idDatPhong;

    // Quan hệ: Nhiều biên bản hủy - 1 người hủy (tùy chọn)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_nguoi_huy", referencedColumnName = "id_nhan_vien")
    private NhanVien nguoiHuy;

    // Quan hệ: Nhiều biên bản hủy - 1 mã giảm giá (tùy chọn)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_ma_giam_gia", referencedColumnName = "id_ma_giam_gia")
    private MaGiamGia maGiamGia;

    @Column(name = "so_tien_hoan", nullable = false, precision = 14, scale = 0)
    private BigDecimal soTienHoan = BigDecimal.ZERO;

    @Nationalized
    @Column(name = "ghi_chu", length = 255)
    private String ghiChu;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DatPhong getDatPhong() { return datPhong; }
    public void setDatPhong(DatPhong datPhong) { this.datPhong = datPhong; }
    public Long getIdDatPhong() { return datPhong != null ? datPhong.getId() : null; }
    public void setIdDatPhong(Long idDatPhong) {
        if (idDatPhong != null) {
            if (this.datPhong == null) this.datPhong = new DatPhong();
            this.datPhong.setId(idDatPhong);
        } else {
            this.datPhong = null;
        }
    }

    public NhanVien getNguoiHuy() { return nguoiHuy; }
    public void setNguoiHuy(NhanVien nguoiHuy) { this.nguoiHuy = nguoiHuy; }
    public Long getIdNguoiHuy() { return nguoiHuy != null ? nguoiHuy.getIdNhanVien() : null; }
    public void setIdNguoiHuy(Long idNguoiHuy) {
        if (idNguoiHuy != null) {
            if (this.nguoiHuy == null) this.nguoiHuy = new NhanVien();
            this.nguoiHuy.setIdNhanVien(idNguoiHuy);
        } else {
            this.nguoiHuy = null;
        }
    }

    public MaGiamGia getMaGiamGia() { return maGiamGia; }
    public void setMaGiamGia(MaGiamGia maGiamGia) { this.maGiamGia = maGiamGia; }
    public Long getIdMaGiamGia() { return maGiamGia != null ? maGiamGia.getId() : null; }
    public void setIdMaGiamGia(Long idMaGiamGia) {
        if (idMaGiamGia != null) {
            if (this.maGiamGia == null) this.maGiamGia = new MaGiamGia();
            this.maGiamGia.setId(idMaGiamGia);
        } else {
            this.maGiamGia = null;
        }
    }
    public BigDecimal getSoTienHoan() { return soTienHoan; }
    public void setSoTienHoan(BigDecimal soTienHoan) { this.soTienHoan = soTienHoan; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
