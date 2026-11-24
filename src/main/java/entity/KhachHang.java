package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

/** Bảng khach_hang (module "Quản lý khách hàng" của bạn khác sẽ mở rộng; ở đây chỉ map đủ để đặt phòng). */
@Entity
@Table(name = "khach_hang")
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_khach_hang")
    private Long id;

    // Quan hệ: 1 khách hàng - 1 người dùng (tài khoản hệ thống)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung", referencedColumnName = "id_nguoi_dung", unique = true)
    private NguoiDung nguoiDung;

    @Column(name = "id_nguoi_dung", insertable = false, updatable = false)
    private Long idNguoiDung;

    @Nationalized
    @Column(name = "ho_ten", nullable = false, length = 150)
    private String hoTen;

    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    @Column(name = "email", length = 150)
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public NguoiDung getNguoiDung() { return nguoiDung; }
    public void setNguoiDung(NguoiDung nguoiDung) { this.nguoiDung = nguoiDung; }
    public Long getIdNguoiDung() { return nguoiDung != null ? nguoiDung.getIdNguoiDung() : idNguoiDung; }
    public void setIdNguoiDung(Long idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
        if (idNguoiDung != null) {
            if (this.nguoiDung == null) this.nguoiDung = new NguoiDung();
            this.nguoiDung.setIdNguoiDung(idNguoiDung);
        } else {
            this.nguoiDung = null;
        }
    }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Trạng thái lấy từ bảng người dùng (quan hệ 1-1)
    public String getTrangThai() {
        return nguoiDung != null ? nguoiDung.getTrangThai() : "Hoạt động";
    }
    public void setTrangThai(String trangThai) {
        if (this.nguoiDung != null) {
            this.nguoiDung.setTrangThai(trangThai);
        }
    }

    // Compatibility aliases for KhachHangServlet, KhachHangRepository, and quan-ly-khach-hang/view.jsp
    public Long getId_khach_hang() { return id; }
    public void setId_khach_hang(Long id) { this.id = id; }
    public String getHo_ten() { return hoTen; }
    public void setHo_ten(String hoTen) { this.hoTen = hoTen; }
    public String getSo_dien_thoai() { return soDienThoai; }
    public void setSo_dien_thoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getTrang_thai() { return getTrangThai(); }
    public void setTrang_thai(String trangThai) { setTrangThai(trangThai); }

    // Compatibility alias for quan-ly-danh-gia
    public Long getIdKhachHang() { return id; }
    public void setIdKhachHang(Long id) { this.id = id; }
}
