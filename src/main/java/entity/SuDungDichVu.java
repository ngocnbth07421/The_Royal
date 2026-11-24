package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "su_dung_dich_vu")
public class SuDungDichVu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_su_dung_dich_vu")
    private Long id_su_dung_dich_vu;

    // Quan hệ: Nhiều sử dụng dịch vụ - 1 đặt phòng chi tiết
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dat_phong_chi_tiet", referencedColumnName = "id_dat_phong_chi_tiet", nullable = false)
    private DatPhongChiTiet datPhongChiTiet;

    // Quan hệ: Nhiều sử dụng dịch vụ - 1 dịch vụ
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dich_vu", referencedColumnName = "id_dich_vu", nullable = false)
    private DichVu dichVu;

    @Column(name = "so_luong")
    private BigDecimal so_luong; // DB là numeric(10, 2) nên dùng BigDecimal

    @Column(name = "thanh_tien")
    private BigDecimal thanh_tien;

    @Column(name = "tien_giam_gia")
    private BigDecimal tien_giam_gia;

    @Column(name = "thoi_gian_su_dung")
    private LocalDateTime thoi_gian_su_dung;

    public Long getId_dat_phong() {
        return datPhongChiTiet != null ? datPhongChiTiet.getId() : null;
    }

    public void setId_dat_phong(Long id) {
        if (id != null) {
            if (this.datPhongChiTiet == null) this.datPhongChiTiet = new DatPhongChiTiet();
            this.datPhongChiTiet.setId(id);
        } else {
            this.datPhongChiTiet = null;
        }
    }

    public Long getId_dich_vu() {
        return dichVu != null ? dichVu.getId_dich_vu() : null;
    }

    public void setId_dich_vu(Long id) {
        if (id != null) {
            if (this.dichVu == null) this.dichVu = new DichVu();
            this.dichVu.setId_dich_vu(id);
        } else {
            this.dichVu = null;
        }
    }
}