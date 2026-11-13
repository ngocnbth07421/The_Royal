package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dich_vu")
public class dichvu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dich_vu")
    private Long id_dich_vu;

    @Column(name = "ma_dich_vu")
    private String ma_dich_vu;

    @Column(name = "ten_dich_vu")
    private String ten_dich_vu;

    @Column(name = "danh_muc")
    private String danh_muc;

    @Column(name = "don_vi_tinh")
    private String don_vi_tinh;

    @Column(name = "don_gia")
    private BigDecimal don_gia;

    @Column(name = "trang_thai")
    private String trang_thai;
}