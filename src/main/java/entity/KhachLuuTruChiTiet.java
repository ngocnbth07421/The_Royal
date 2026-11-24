package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "khach_luu_tru_chi_tiet")
public class KhachLuuTruChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_khach_luu_tru")
    private Long idKhachLuuTru;

    // Quan hệ: Nhiều khách lưu trú chi tiết - 1 đặt phòng chi tiết
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dat_phong_chi_tiet", referencedColumnName = "id_dat_phong_chi_tiet", nullable = false)
    private DatPhongChiTiet datPhongChiTiet;

    @Column(name = "ho_ten", length = 150, nullable = false)
    private String hoTen;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "quoc_tich", length = 60)
    private String quocTich;

    @Column(name = "loai_giay_to", length = 20, nullable = false)
    private String loaiGiayTo = "CCCD";

    @Column(name = "so_giay_to", columnDefinition = "NVARCHAR(MAX)")
    private String soGiayTo;

    @Column(name = "la_tre_em", nullable = false)
    private Boolean laTreEm = false;
}
