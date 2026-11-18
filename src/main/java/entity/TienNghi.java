package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "tien_nghi")
public class TienNghi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tien_nghi")
    private Long id;

    @Nationalized
    @Column(name = "ten_tien_nghi", nullable = false, length = 100, unique = true)
    private String tenTienNghi;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong = 1;

    public TienNghi() {}
    public TienNghi(String tenTienNghi, Integer soLuong) { this.tenTienNghi = tenTienNghi; this.soLuong = soLuong; }

    /** Mã riêng của tiện nghi, sinh từ id (TN001, TN002, ...). Không cần thêm cột trong DB. */
    public String getMaTienNghi() { return id == null ? "" : String.format("TN%03d", id); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenTienNghi() { return tenTienNghi; }
    public void setTenTienNghi(String tenTienNghi) { this.tenTienNghi = tenTienNghi; }
    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }
}
