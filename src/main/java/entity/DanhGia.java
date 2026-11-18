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
    @Column(name = "id_dat_phong")
    private Integer idDatPhong;
    @Column(name = "id_khach_hang")
    private Integer idKhachHang;
    @Column(name = "diem_danh_gia")
    private Integer diemDanhGia;
    @Column(name = "binh_luan")
    private String binhLuan;
    @Column(name = "bi_an")
    private Boolean biAn;
    @Column(name = "ly_do_an")
    private String lyDoAn;

}
