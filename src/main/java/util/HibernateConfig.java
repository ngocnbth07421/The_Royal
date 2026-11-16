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
        properties.put(Environment.PASS, "81129");
        properties.put(Environment.SHOW_SQL, "true");

        // Khai báo các Entity class ánh xạ xuống CSDL THE_ROYAL
        conf.addAnnotatedClass(ToaNha.class);
        conf.addAnnotatedClass(Tang.class);
        conf.addAnnotatedClass(LoaiPhong.class);
        conf.addAnnotatedClass(Phong.class);
        conf.addAnnotatedClass(VaiTro.class);
        conf.addAnnotatedClass(NhanVien.class);
        conf.addAnnotatedClass(NhiemVuDonPhong.class);
        conf.addAnnotatedClass(TienNghiPhong.class);
        conf.addAnnotatedClass(TienNghi.class);
        conf.addAnnotatedClass(NguoiDung.class);
        conf.addAnnotatedClass(KhachHang.class);

        conf.setProperties(properties);
        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(conf.getProperties()).build();
        FACTORY = conf.buildSessionFactory(registry);
    }

    public static SessionFactory getFACTORY() {
        return FACTORY;
    }

    public static void main(String[] args) {
        System.out.println(getFACTORY());
    }
}
