
package repository;

import entity.khachhang;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.List;

public class Khachhangrepository {

    // Lấy danh sách khách hàng
    public List<khachhang> getAll() {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery("from khachhang", khachhang.class).list();
        }
    }

    // Lấy khách hàng theo ID
    public khachhang getOne(Long id) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.find(khachhang.class, id);
        }
    }

    // Thêm khách hàng
    public void add(khachhang kh) {
        Transaction transaction = null;

        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();

            session.persist(kh);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi thêm khách hàng", e);
        }
    }

    // Sửa khách hàng
    public void update(khachhang kh) {
        Transaction transaction = null;

        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();

            khachhang khCu = session.find(
                    khachhang.class,
                    kh.getId_khach_hang()
            );

            if (khCu == null) {
                throw new RuntimeException("Không tìm thấy khách hàng ID: "
                        + kh.getId_khach_hang());
            }

            khCu.setHo_ten(kh.getHo_ten());
            khCu.setSo_dien_thoai(kh.getSo_dien_thoai());
            khCu.setEmail(kh.getEmail());
            khCu.setTrang_thai(kh.getTrang_thai());

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi cập nhật khách hàng", e);
        }
    }

    // Xóa khách hàng
    public void delete(Long id) {
        Transaction transaction = null;

        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();

            khachhang kh = session.find(khachhang.class, id);

            if (kh != null) {
                session.remove(kh);
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi xóa khách hàng", e);
        }
    }
}
