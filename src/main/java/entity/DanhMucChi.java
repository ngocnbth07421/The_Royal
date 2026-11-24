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
@Table(name = "danh_muc_chi")
public class DanhMucChi {
    public static final String NHAN_SU   = "Nhân sự";
    public static final String VAT_TU    = "Vật tư";
    public static final String KHAC      = "Khác";
    public static final String VAN_HANH  = "Vận hành";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_danh_muc")
    private Long idDanhMuc;

    @Column(name = "ten_danh_muc", length = 100, nullable = false, unique = true)
    private String tenDanhMuc;

    @Column(name = "loai_chi_phi", length = 25, nullable = false)
    private String loaiChiPhi;

    @Column(name = "so_tien", nullable = false, precision = 14, scale = 0)
    private BigDecimal soTien = BigDecimal.ZERO;

    @Column(name = "ghi_chu", length = 255)
    private String ghiChu;
}
