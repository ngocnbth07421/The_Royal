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
@Table(name = "bang_thu_chi")
public class BangThuChi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_thu_chi")
    private Long idThuChi;

    // Quan hệ: Nhiều bảng thu chi - 1 danh mục chi
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_danh_muc", referencedColumnName = "id_danh_muc", nullable = false)
    private DanhMucChi danhMucChi;

    // Quan hệ: Nhiều bảng thu chi - 1 bảng lương (tùy chọn)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_bang_luong", referencedColumnName = "id_bang_luong")
    private BangLuong bangLuong;

    // Quan hệ: Nhiều bảng thu chi - 1 nhân viên tạo (tùy chọn)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_nguoi_tao", referencedColumnName = "id_nhan_vien")
    private NhanVien nguoiTao;

    @Column(name = "thoi_gian", nullable = false)
    private LocalDateTime thoiGian = LocalDateTime.now();

    @Column(name = "doanh_thu", nullable = false, precision = 14, scale = 0)
    private BigDecimal doanhThu = BigDecimal.ZERO;

    @Column(name = "ghi_chu", length = 255)
    private String ghiChu;
}
