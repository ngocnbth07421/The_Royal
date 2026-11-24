package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "danh_gia")

public class DanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_danh_gia")
    private Integer idDanhGia;
    // Quan hệ: 1 đánh giá - 1 đặt phòng (UNIQUE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dat_phong", referencedColumnName = "id_dat_phong", nullable = false, unique = true)
    private DatPhong datPhong;

    // Quan hệ: Nhiều đánh giá - 1 khách hàng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang", referencedColumnName = "id_khach_hang", nullable = false)
    private KhachHang khachHang;
    @Column(name = "diem_danh_gia")
    private Integer diemDanhGia;
    @Column(name = "binh_luan")
    private String binhLuan;
    @Column(name = "bi_an")
    private Boolean biAn;
    @Column(name = "ly_do_an")
    private String lyDoAn;

    public DatPhong getDatPhong() { return datPhong; }
    public void setDatPhong(DatPhong datPhong) { this.datPhong = datPhong; }
    public Integer getIdDatPhong() {
        return datPhong != null && datPhong.getId() != null ? datPhong.getId().intValue() : null;
    }
    public void setIdDatPhong(Integer idDatPhong) {
        if (idDatPhong != null) {
            if (this.datPhong == null) this.datPhong = new DatPhong();
            this.datPhong.setId(idDatPhong.longValue());
        } else {
            this.datPhong = null;
        }
    }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }
    public Integer getIdKhachHang() {
        return khachHang != null && khachHang.getId() != null ? khachHang.getId().intValue() : null;
    }
    public void setIdKhachHang(Integer idKhachHang) {
        if (idKhachHang != null) {
            if (this.khachHang == null) this.khachHang = new KhachHang();
            this.khachHang.setId(idKhachHang.longValue());
        } else {
            this.khachHang = null;
        }
    }
}
