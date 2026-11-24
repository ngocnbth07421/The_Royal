package entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dat_phong")
public class DatPhong {
    // 7 trạng thái chi tiết đặt phòng (theo nghiệp vụ khách sạn mới)
    public static final String TAM_GIU           = "Tạm giữ";
    public static final String DA_XAC_NHAN       = "Đã xác nhận";
    public static final String KHACH_DANG_O      = "Khách đang ở";
    public static final String DA_TRA_PHONG      = "Đã trả phòng";
    public static final String DA_HUY            = "Đã hủy";
    public static final String KHONG_DEN         = "Không đến";
    public static final String KHONG_THANH_CONG  = "Không thành công";

    // Hằng số tương thích ngược với code cũ
    public static final String CHO_COC           = "Tạm giữ";
    public static final String DANG_LUU_TRU      = "Khách đang ở";
    public static final String KHACH_HUY         = "Đã hủy";
    public static final String KHACH_SAN_HUY     = "Đã hủy";
    public static final String TU_HUY            = "Không đến";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dat_phong")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_khach_hang", nullable = false)
    private KhachHang khachHang;

    // Quan hệ: Nhiều đặt phòng - 1 nhân viên tạo (tùy chọn)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_nguoi_tao", referencedColumnName = "id_nhan_vien")
    private NhanVien nguoiTao;

    // Quan hệ: Nhiều đặt phòng - 1 mã giảm giá (tùy chọn)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_ma_giam_gia", referencedColumnName = "id_ma_giam_gia")
    private MaGiamGia maGiamGia;

    @Column(name = "ma_dat_phong", nullable = false, length = 20, unique = true)
    private String maDatPhong;

    // Cột trang_thai đã xóa ở bảng dat_phong SQL, chuyển sang trường tạm (transient) lấy từ chi tiết
    @Transient
    private String trangThai = TAM_GIU;

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
    public NhanVien getNguoiTao() { return nguoiTao; }
    public void setNguoiTao(NhanVien nguoiTao) { this.nguoiTao = nguoiTao; }
    public Long getIdNguoiTao() { return nguoiTao != null ? nguoiTao.getIdNhanVien() : null; }
    public void setIdNguoiTao(Long idNguoiTao) {
        if (idNguoiTao != null) {
            if (this.nguoiTao == null) this.nguoiTao = new NhanVien();
            this.nguoiTao.setIdNhanVien(idNguoiTao);
        } else {
            this.nguoiTao = null;
        }
    }

    public MaGiamGia getMaGiamGia() { return maGiamGia; }
    public void setMaGiamGia(MaGiamGia maGiamGia) { this.maGiamGia = maGiamGia; }
    public Long getIdMaGiamGia() { return maGiamGia != null ? maGiamGia.getId() : null; }
    public void setIdMaGiamGia(Long idMaGiamGia) {
        if (idMaGiamGia != null) {
            if (this.maGiamGia == null) this.maGiamGia = new MaGiamGia();
            this.maGiamGia.setId(idMaGiamGia);
        } else {
            this.maGiamGia = null;
        }
    }
    public String getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(String maDatPhong) { this.maDatPhong = maDatPhong; }
    public String getTrangThai() {
        if (trangThai != null) return trangThai;
        if (chiTiet != null && !chiTiet.isEmpty()) {
            return chiTiet.get(0).getTrangThai();
        }
        return TAM_GIU;
    }
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        if (this.chiTiet != null) {
            for (DatPhongChiTiet ct : this.chiTiet) {
                ct.setTrangThai(trangThai);
            }
        }
    }
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
