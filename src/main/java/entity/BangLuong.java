package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bang_luong")
public class BangLuong {
    public static final String CHO_CAP_NHAT = "Chờ cập nhật";
    public static final String DA_XAC_NHAN  = "Đã xác nhận";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bang_luong")
    private Long idBangLuong;

    // Quan hệ: Nhiều bảng lương - 1 nhân viên
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_nhan_vien", referencedColumnName = "id_nhan_vien", nullable = false)
    private NhanVien nhanVien;

    @Column(name = "thoi_gian_nhan", nullable = false)
    private LocalDate thoiGianNhan;

    @Column(name = "luong_co_ban", nullable = false, precision = 14, scale = 0)
    private BigDecimal luongCoBan;

    @Column(name = "phu_cap", nullable = false, precision = 14, scale = 0)
    private BigDecimal phuCap = BigDecimal.ZERO;

    @Column(name = "khau_tru", nullable = false, precision = 14, scale = 0)
    private BigDecimal khauTru = BigDecimal.ZERO;

    @Column(name = "luong_tong", nullable = false, precision = 14, scale = 0)
    private BigDecimal luongTong;

    @Column(name = "trang_thai", length = 25, nullable = false)
    private String trangThai = CHO_CAP_NHAT;
}
