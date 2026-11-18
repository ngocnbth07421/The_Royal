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
@Table(name = "khach_hang")
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_khach_hang")
    private Long id_khach_hang; // Trùng kiểu bigint trong SQL Server thì nên dùng Long

    @Column(name = "ho_ten")
    private String ho_ten;

    @Column(name = "so_dien_thoai")
    private String so_dien_thoai;

    @Column(name = "email")
    private String email;

    @Column(name = "trang_thai")
    private String trang_thai;
}
