package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nhiem_vu_don_phong")
public class NhiemVuDonPhong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nhiem_vu")
    private Long idNhiemVu;

    // Quan hệ: Nhiều nhiệm vụ - 1 phòng
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_phong", referencedColumnName = "id_phong", nullable = false)
    private Phong phong;

    // Quan hệ: Nhiều nhiệm vụ - 1 đặt phòng chi tiết
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dat_phong_chi_tiet", referencedColumnName = "id_dat_phong_chi_tiet", nullable = false)
    private DatPhongChiTiet datPhongChiTiet;

    // Quan hệ: Nhiều nhiệm vụ - 1 nhân viên (tùy chọn)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_nhan_vien", referencedColumnName = "id_nhan_vien")
    private NhanVien nhanVien;

    @Column(name = "trang_thai", length = 20, nullable = false)
    private String trangThai;

    public Long getIdNhiemVu() { return idNhiemVu; }
    public void setIdNhiemVu(Long idNhiemVu) { this.idNhiemVu = idNhiemVu; }

    public Phong getPhong() { return phong; }
    public void setPhong(Phong phong) { this.phong = phong; }
    public Long getIdPhong() { return phong != null ? phong.getId() : null; }
    public void setIdPhong(Long idPhong) {
        if (idPhong != null) {
            if (this.phong == null) this.phong = new Phong();
            this.phong.setId(idPhong);
        } else {
            this.phong = null;
        }
    }

    public DatPhongChiTiet getDatPhongChiTiet() { return datPhongChiTiet; }
    public void setDatPhongChiTiet(DatPhongChiTiet datPhongChiTiet) { this.datPhongChiTiet = datPhongChiTiet; }
    public Long getIdDatPhongChiTiet() { return datPhongChiTiet != null ? datPhongChiTiet.getId() : null; }
    public void setIdDatPhongChiTiet(Long idDatPhongChiTiet) {
        if (idDatPhongChiTiet != null) {
            if (this.datPhongChiTiet == null) this.datPhongChiTiet = new DatPhongChiTiet();
            this.datPhongChiTiet.setId(idDatPhongChiTiet);
        } else {
            this.datPhongChiTiet = null;
        }
    }

    public NhanVien getNhanVien() { return nhanVien; }
    public void setNhanVien(NhanVien nhanVien) { this.nhanVien = nhanVien; }
    public Long getIdNhanVien() { return nhanVien != null ? nhanVien.getIdNhanVien() : null; }
    public void setIdNhanVien(Long idNhanVien) {
        if (idNhanVien != null) {
            if (this.nhanVien == null) this.nhanVien = new NhanVien();
            this.nhanVien.setIdNhanVien(idNhanVien);
        } else {
            this.nhanVien = null;
        }
    }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
