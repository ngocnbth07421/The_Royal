package entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dat_phong")
public class DatPhong {
    public static final String CHO_COC        = "CHO_COC";
    public static final String DA_XAC_NHAN    = "DA_XAC_NHAN";
    public static final String DANG_LUU_TRU   = "DANG_LUU_TRU";
    public static final String DA_TRA_PHONG   = "DA_TRA_PHONG";
    public static final String KHACH_HUY      = "KHACH_HUY";
    public static final String KHACH_SAN_HUY  = "KHACH_SAN_HUY";
    public static final String TU_HUY         = "TU_HUY";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dat_phong")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_khach_hang", nullable = false)
    private KhachHang khachHang;

    /** id nhân viên tạo; NULL = khách tự đặt. Để id thô để không phụ thuộc entity NhanVien của người khác. */
    @Column(name = "id_nguoi_tao")
    private Long idNguoiTao;

    @Column(name = "id_ma_giam_gia")
    private Long idMaGiamGia;

    @Column(name = "ma_dat_phong", nullable = false, length = 20, unique = true)
    private String maDatPhong;

    @Column(name = "trang_thai", nullable = false, length = 20)
    private String trangThai = CHO_COC;

    @Column(name = "thoi_gian_nhan_phong", nullable = false)
    private LocalDateTime thoiGianNhanPhong;

    @Column(name = "thoi_gian_tra_phong", nullable = false)
    private LocalDateTime thoiGianTraPhong;

    @Column(name = "so_dem", nullable = false)
    private Integer soDem;

    @Column(name = "tien_phong", nullable = false, precision = 14, scale = 0)
    private BigDecimal tienPhong = BigDecimal.ZERO;
    @Column(name = "giam_gia", nullable = false, precision = 14, scale = 0)
    private BigDecimal giamGia = BigDecimal.ZERO;
    @Column(name = "phi_dich_vu", nullable = false, precision = 14, scale = 0)
    private BigDecimal phiDichVu = BigDecimal.ZERO;
    @Column(name = "thue_vat", nullable = false, precision = 14, scale = 0)
    private BigDecimal thueVat = BigDecimal.ZERO;
    @Column(name = "tong_tien", nullable = false, precision = 14, scale = 0)
    private BigDecimal tongTien = BigDecimal.ZERO;
    @Column(name = "tien_coc", nullable = false, precision = 14, scale = 0)
    private BigDecimal tienCoc = BigDecimal.ZERO;
    @Column(name = "tien_thanh_toan", nullable = false, precision = 14, scale = 0)
    private BigDecimal tienThanhToan = BigDecimal.ZERO;

    @OneToMany(mappedBy = "datPhong", cascade = CascadeType.PERSIST)
    private List<DatPhongChiTiet> chiTiet = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdDatPhong() { return id; }
    public void setIdDatPhong(Long id) { this.id = id; }
    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }
    public Long getIdNguoiTao() { return idNguoiTao; }
    public void setIdNguoiTao(Long idNguoiTao) { this.idNguoiTao = idNguoiTao; }
    public Long getIdMaGiamGia() { return idMaGiamGia; }
    public void setIdMaGiamGia(Long idMaGiamGia) { this.idMaGiamGia = idMaGiamGia; }
    public String getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(String maDatPhong) { this.maDatPhong = maDatPhong; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public LocalDateTime getThoiGianNhanPhong() { return thoiGianNhanPhong; }
    public void setThoiGianNhanPhong(LocalDateTime v) { this.thoiGianNhanPhong = v; }
    public LocalDateTime getThoiGianTraPhong() { return thoiGianTraPhong; }
    public void setThoiGianTraPhong(LocalDateTime v) { this.thoiGianTraPhong = v; }
    public Integer getSoDem() { return soDem; }
    public void setSoDem(Integer soDem) { this.soDem = soDem; }
    public BigDecimal getTienPhong() { return tienPhong; }
    public void setTienPhong(BigDecimal v) { this.tienPhong = v; }
    public BigDecimal getGiamGia() { return giamGia; }
    public void setGiamGia(BigDecimal v) { this.giamGia = v; }
    public BigDecimal getPhiDichVu() { return phiDichVu; }
    public void setPhiDichVu(BigDecimal v) { this.phiDichVu = v; }
    public BigDecimal getThueVat() { return thueVat; }
    public void setThueVat(BigDecimal v) { this.thueVat = v; }
    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal v) { this.tongTien = v; }
    public BigDecimal getTienCoc() { return tienCoc; }
    public void setTienCoc(BigDecimal v) { this.tienCoc = v; }
    public BigDecimal getTienThanhToan() { return tienThanhToan; }
    public void setTienThanhToan(BigDecimal v) { this.tienThanhToan = v; }
    public List<DatPhongChiTiet> getChiTiet() { return chiTiet; }
    public void setChiTiet(List<DatPhongChiTiet> chiTiet) { this.chiTiet = chiTiet; }
}
