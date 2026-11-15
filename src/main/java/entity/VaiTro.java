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
@Table(name = "vai_tro")
public class VaiTro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vai_tro")
    private Long idVaiTro;

    @Column(name = "ma_vai_tro", length = 30, nullable = false, unique = true)
    private String maVaiTro;

    @Column(name = "ten_vai_tro", length = 100, nullable = false)
    private String tenVaiTro;

    public Long getIdVaiTro() { return idVaiTro; }
    public void setIdVaiTro(Long idVaiTro) { this.idVaiTro = idVaiTro; }

    public String getMaVaiTro() { return maVaiTro; }
    public void setMaVaiTro(String maVaiTro) { this.maVaiTro = maVaiTro; }

    public String getTenVaiTro() { return tenVaiTro; }
    public void setTenVaiTro(String tenVaiTro) { this.tenVaiTro = tenVaiTro; }
}
