package util;

import entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;


import java.util.Properties;

/*
    - SessionFactory là một Interface cốt lõi của Hibernate.
    Nhiệm vụ của nó là tạo ra các đối tượng Session (phiên làm việc).
    Mỗi Session đại diện cho một kết nối tới database để thực hiện các thao tác thêm, sửa, xóa, lấy dữ liệu.
    - Tạo đối tượng Configuration để chứa toàn bộ thông tin cấu hình của Hibernate.
    - Tạo đối tượng Properties để lưu trữ các thông số kết nối Database dưới dạng key-value.
 */
public class HibernateConfig {
    private static final SessionFactory FACTORY;

    static {
        Configuration conf = new Configuration();

        Properties properties = new Properties();
        properties.put(Environment.DIALECT, "org.hibernate.dialect.SQLServer2016Dialect");
        properties.put(Environment.DRIVER, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        properties.put(Environment.URL, "jdbc:sqlserver://localhost:1433;databaseName=THE_ROYAL;encrypt=true;trustServerCertificate=true;");
        properties.put(Environment.USER, "sa");
        properties.put(Environment.PASS, "abc123");
        properties.put(Environment.SHOW_SQL, "true");
        /*
            - Khai báo cho Hibernate biết các Class nào trong Java sẽ được ánh xạ xuống database
            - Khi ứng dụng chạy, Hibernate sẽ quét các class xxx1 và xxx2 (thường có annotation @Entity) để quản lý chúng.
        */
        conf.addAnnotatedClass(TienNghi.class);
        conf.addAnnotatedClass(DatPhong.class);
        conf.addAnnotatedClass(DatPhongChiTiet.class);
        conf.addAnnotatedClass(KhachHang.class);
        conf.addAnnotatedClass(LichSuDatPhong.class);
        conf.addAnnotatedClass(Phong.class);
        conf.addAnnotatedClass(LoaiPhong.class);
        conf.addAnnotatedClass(HangPhong.class);
        conf.addAnnotatedClass(LoaiGiuong.class);
        conf.addAnnotatedClass(DanhGia.class);
        conf.addAnnotatedClass(DichVu.class);
        conf.addAnnotatedClass(SuDungDichVu.class);
        conf.addAnnotatedClass(MaGiamGia.class);
        conf.addAnnotatedClass(NguoiDung.class);
        conf.addAnnotatedClass(NhanVien.class);
        conf.addAnnotatedClass(NhiemVuDonPhong.class);
        conf.addAnnotatedClass(Tang.class);
        conf.addAnnotatedClass(ThanhToan.class);
        conf.addAnnotatedClass(TheThanhVien.class);
        conf.addAnnotatedClass(TienNghiPhong.class);
        conf.addAnnotatedClass(ToaNha.class);
        conf.addAnnotatedClass(VaiTro.class);
        conf.addAnnotatedClass(HangThanhVien.class);
        conf.addAnnotatedClass(KhachLuuTruChiTiet.class);
        conf.addAnnotatedClass(LichSuDoiPhong.class);
        conf.addAnnotatedClass(BangLuong.class);
        conf.addAnnotatedClass(DanhMucChi.class);
        conf.addAnnotatedClass(BangThuChi.class);
        conf.setProperties(properties);
        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(conf.getProperties()).build();
        //dùng Configuration và ServiceRegistry để "đúc" ra FACTORY- một đối tượng SessionFactory
        FACTORY = conf.buildSessionFactory(registry);

    }

    public static SessionFactory getFACTORY() {
        return FACTORY;
    }

    public static void main(String[] args) {
        System.out.println(getFACTORY());
    }
}
