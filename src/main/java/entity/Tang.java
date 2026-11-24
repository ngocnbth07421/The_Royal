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
@Table(name = "tang")
public class Tang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tang")
    private Long idTang;

    // Quan hệ: Nhiều tầng - 1 tòa nhà
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_toa_nha", referencedColumnName = "id_toa_nha", nullable = false)
    private ToaNha toaNha;

    @Column(name = "so_tang", nullable = false)
    private Integer soTang;

    public ToaNha getToaNha() { return toaNha; }
    public void setToaNha(ToaNha toaNha) { this.toaNha = toaNha; }

    public Long getIdToaNha() { return toaNha != null ? toaNha.getIdToaNha() : null; }
    public void setIdToaNha(Long idToaNha) {
        if (idToaNha != null) {
            if (this.toaNha == null) this.toaNha = new ToaNha();
            this.toaNha.setIdToaNha(idToaNha);
        } else {
            this.toaNha = null;
        }
    }

}
