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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lich_su_doi_phong")
public class LichSuDoiPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lich_su_doi_phong")
    private Long idLichSuDoiPhong;

    // Quan hệ: Nhiều lịch sử đổi phòng - 1 đặt phòng chi tiết
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dat_phong_chi_tiet", referencedColumnName = "id_dat_phong_chi_tiet", nullable = false)
    private DatPhongChiTiet datPhongChiTiet;

    // Quan hệ: Nhiều lịch sử đổi phòng - 1 phòng cũ
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_phong_cu", referencedColumnName = "id_phong", nullable = false)
    private Phong phongCu;

    // Quan hệ: Nhiều lịch sử đổi phòng - 1 phòng mới
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_phong_moi", referencedColumnName = "id_phong", nullable = false)
    private Phong phongMoi;

    // Quan hệ: Nhiều lịch sử đổi phòng - 1 nhân viên tạo (tùy chọn)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_nguoi_tao", referencedColumnName = "id_nhan_vien")
    private NhanVien nguoiTao;

    @Column(name = "doi_luc", nullable = false)
    private LocalDateTime doiLuc = LocalDateTime.now();

    @Column(name = "chenh_lech_gia", nullable = false, precision = 14, scale = 0)
    private BigDecimal chenhLechGia = BigDecimal.ZERO;

    @Column(name = "ly_do", length = 255)
    private String lyDo;
}
