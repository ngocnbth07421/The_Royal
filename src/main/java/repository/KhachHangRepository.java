package repository;

import entity.KhachHang;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.List;

public class KhachHangRepository {

    // Lấy danh sách khách hàng
    public List<KhachHang> getAll() {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery("FROM KhachHang ORDER BY id_khach_hang ASC", KhachHang.class).list();
        }
    }

    // Lấy khách hàng theo ID
    public KhachHang getOne(Long id) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.find(KhachHang.class, id);
        }
    }

    public KhachHang findByEmail(String email) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery("FROM KhachHang WHERE email = :email", KhachHang.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
    }

    public KhachHang findBySoDienThoai(String soDienThoai) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery("FROM KhachHang WHERE so_dien_thoai = :soDienThoai", KhachHang.class)
                    .setParameter("soDienThoai", soDienThoai)
                    .uniqueResult();
        }
    }

    // Thêm khách hàng
    public void add(KhachHang kh) {
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
    public void update(KhachHang kh) {
        Transaction transaction = null;

        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();

            KhachHang khCu = session.find(
                    KhachHang.class,
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

            KhachHang kh = session.find(KhachHang.class, id);

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
