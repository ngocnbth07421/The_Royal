package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
class TienNghiPhongId implements Serializable {
    private Long idPhong;
    private Long idTienNghi;

    public Long getIdPhong() { return idPhong; }
    public void setIdPhong(Long idPhong) { this.idPhong = idPhong; }
    public Long getIdTienNghi() { return idTienNghi; }
    public void setIdTienNghi(Long idTienNghi) { this.idTienNghi = idTienNghi; }
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tien_nghi_phong")
@IdClass(TienNghiPhongId.class)
public class TienNghiPhong {
    @Id
    @Column(name = "id_phong")
    private Long idPhong;

    @Id
    @Column(name = "id_tien_nghi")
    private Long idTienNghi;

    public Long getIdPhong() { return idPhong; }
    public void setIdPhong(Long idPhong) { this.idPhong = idPhong; }

    public Long getIdTienNghi() { return idTienNghi; }
    public void setIdTienNghi(Long idTienNghi) { this.idTienNghi = idTienNghi; }
}
