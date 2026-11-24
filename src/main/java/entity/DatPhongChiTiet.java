package entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "dat_phong_chi_tiet")
public class DatPhongChiTiet {
    // 7 trạng thái chi tiết đặt phòng mới
    public static final String TAM_GIU          = "Tạm giữ";
    public static final String DA_XAC_NHAN      = "Đã xác nhận";
    public static final String KHACH_DANG_O     = "Khách đang ở";
    public static final String DA_TRA_PHONG     = "Đã trả phòng";
    public static final String DA_HUY           = "Đã hủy";
    public static final String KHONG_DEN        = "Không đến";
    public static final String KHONG_THANH_CONG = "Không thành công";

    // Hằng số tương thích ngược
    public static final String HIEU_LUC         = "Đã xác nhận";
    public static final String DANG_LUU_TRU     = "Khách đang ở";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dat_phong_chi_tiet")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dat_phong", nullable = false)
    private DatPhong datPhong;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_phong", nullable = false)
    private Phong phong;

    @Column(name = "so_nguoi_lon", nullable = false)
    private Integer soNguoiLon = 1;
    @Column(name = "so_tre_em_duoi_6", nullable = false)
    private Integer soTreEmDuoi6 = 0;
    @Column(name = "so_tre_em_tu_6_tuoi", nullable = false)
    private Integer soTreEmTu6Tuoi = 0;

    @Column(name = "trang_thai", nullable = false, length = 30)
    private String trangThai = TAM_GIU;

    @Column(name = "ngay_nhan", nullable = false)
    private LocalDate ngayNhan;
    @Column(name = "ngay_tra", nullable = false)
    private LocalDate ngayTra;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DatPhong getDatPhong() { return datPhong; }
    public void setDatPhong(DatPhong datPhong) { this.datPhong = datPhong; }
    public Phong getPhong() { return phong; }
    public void setPhong(Phong phong) { this.phong = phong; }
    public Integer getSoNguoiLon() { return soNguoiLon; }
    public void setSoNguoiLon(Integer v) { this.soNguoiLon = v; }
    public Integer getSoTreEmDuoi6() { return soTreEmDuoi6; }
    public void setSoTreEmDuoi6(Integer v) { this.soTreEmDuoi6 = v; }
    public Integer getSoTreEmTu6Tuoi() { return soTreEmTu6Tuoi; }
    public void setSoTreEmTu6Tuoi(Integer v) { this.soTreEmTu6Tuoi = v; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public LocalDate getNgayNhan() { return ngayNhan; }
    public void setNgayNhan(LocalDate ngayNhan) { this.ngayNhan = ngayNhan; }
    public LocalDate getNgayTra() { return ngayTra; }
    public void setNgayTra(LocalDate ngayTra) { this.ngayTra = ngayTra; }
}
