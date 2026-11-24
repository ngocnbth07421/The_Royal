package repository;

import entity.DatPhong;
import entity.ThanhToan;
import entity.TheThanhVien;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.List;

public class ThanhToanRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<ThanhToan> findAll() {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM ThanhToan ORDER BY id DESC", ThanhToan.class).list();
        }
    }

    public ThanhToan findById(Long id) {
        try (Session session = getSession()) {
            return session.find(ThanhToan.class, id);
        }
    }

    public List<DatPhong> findAllDatPhong() {
        try (Session session = getSession()) {
            return session.createQuery("FROM DatPhong ORDER BY id DESC", DatPhong.class).list();
        }
    }

    public List<TheThanhVien> findAllTheThanhVienHoatDong() {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM TheThanhVien WHERE trangThai = :tt", TheThanhVien.class)
                    .setParameter("tt", "Hoạt động")
                    .list();
        }
    }

    public void saveOrUpdate(ThanhToan tt) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            if (tt.getId() == null) {
                session.persist(tt);
            } else {
                session.merge(tt);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Lỗi lưu thanh toán", e);
        }
    }

    public void capNhatTrangThai(Long id, String trangThaiMoi) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            ThanhToan tt = session.find(ThanhToan.class, id);
            if (tt != null) {
                tt.setTrangThai(trangThaiMoi);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Lỗi cập nhật trạng thái thanh toán", e);
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            ThanhToan tt = session.find(ThanhToan.class, id);
            if (tt != null) {
                session.remove(tt);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Lỗi xóa thanh toán", e);
        }
    }
}
