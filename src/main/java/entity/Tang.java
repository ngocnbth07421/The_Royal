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

    @Column(name = "id_toa_nha", nullable = false)
    private Long idToaNha;

    @Column(name = "so_tang", nullable = false)
    private Integer soTang;

}
