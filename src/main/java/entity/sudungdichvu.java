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
public class sudungdichvu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_su_dung_dich_vu")
    private Long id_su_dung_dich_vu;

    @Column(name = "id_dat_phong")
    private Long id_dat_phong;

    @Column(name = "id_dich_vu")
    private Long id_dich_vu;

    @Column(name = "so_luong")
    private BigDecimal so_luong; // DB là numeric(10, 2) nên dùng BigDecimal

    @Column(name = "thanh_tien")
    private BigDecimal thanh_tien;

    @Column(name = "tien_giam_gia")
    private BigDecimal tien_giam_gia;

    @Column(name = "thoi_gian_su_dung")
    private LocalDateTime thoi_gian_su_dung;
}