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

    @Column(name = "id_phong", nullable = false)
    private Long idPhong;

    @Column(name = "id_dat_phong_chi_tiet")
    private Long idDatPhongChiTiet;

    @Column(name = "id_nhan_vien")
    private Long idNhanVien;

    @Column(name = "trang_thai", length = 20, nullable = false)
    private String trangThai;

    public Long getIdNhiemVu() { return idNhiemVu; }
    public void setIdNhiemVu(Long idNhiemVu) { this.idNhiemVu = idNhiemVu; }

    public Long getIdPhong() { return idPhong; }
    public void setIdPhong(Long idPhong) { this.idPhong = idPhong; }

    public Long getIdDatPhongChiTiet() { return idDatPhongChiTiet; }
    public void setIdDatPhongChiTiet(Long idDatPhongChiTiet) { this.idDatPhongChiTiet = idDatPhongChiTiet; }

    public Long getIdNhanVien() { return idNhanVien; }
    public void setIdNhanVien(Long idNhanVien) { this.idNhanVien = idNhanVien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
