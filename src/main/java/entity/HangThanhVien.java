package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hang_thanh_vien")
public class HangThanhVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hang_thanh_vien")
    private Long idHangThanhVien;

    @Column(name = "ma_hang_thanh_vien", length = 20, nullable = false, unique = true)
    private String maHangThanhVien;

    @Column(name = "ten_hang_thanh_vien", length = 50, nullable = false)
    private String tenHangThanhVien;

    @Column(name = "so_dem_toi_thieu", nullable = false)
    private Integer soDemToiThieu;

    @Column(name = "muc_nap_toi_thieu", nullable = false, precision = 14, scale = 0)
    private BigDecimal mucNapToiThieu;

    @Column(name = "phan_tram_giam_gia_phong", nullable = false)
    private Short phanTramGiamGiaPhong;

    @Column(name = "phan_tram_giam_gia_dich_vu", nullable = false)
    private Short phanTramGiamGiaDichVu;
}
