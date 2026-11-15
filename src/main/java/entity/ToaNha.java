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
@Table(name = "toa_nha")
public class ToaNha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_toa_nha")
    private Long idToaNha;

    @Column(name = "ma_toa_nha", length = 5, nullable = false, unique = true)
    private String maToaNha;

    @Column(name = "ten", length = 100, nullable = false)
    private String ten;

    public Long getIdToaNha() { return idToaNha; }
    public void setIdToaNha(Long idToaNha) { this.idToaNha = idToaNha; }

    public String getMaToaNha() { return maToaNha; }
    public void setMaToaNha(String maToaNha) { this.maToaNha = maToaNha; }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }
}
