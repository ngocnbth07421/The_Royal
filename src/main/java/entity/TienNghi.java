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
@Table(name = "tien_nghi")
public class TienNghi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tien_nghi")
    private Long idTienNghi;

    @Column(name = "ten_tien_nghi", length = 100, nullable = false)
    private String tenTienNghi;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    public Long getIdTienNghi() { return idTienNghi; }
    public void setIdTienNghi(Long idTienNghi) { this.idTienNghi = idTienNghi; }

    public String getTenTienNghi() { return tenTienNghi; }
    public void setTenTienNghi(String tenTienNghi) { this.tenTienNghi = tenTienNghi; }

    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }
}
