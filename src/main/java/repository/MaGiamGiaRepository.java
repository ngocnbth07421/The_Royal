package repository;

import entity.MaGiamGia;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.List;

public class MaGiamGiaRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<MaGiamGia> findAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM MaGiamGia ORDER BY id DESC", MaGiamGia.class).list();
        }
    }

    public MaGiamGia findById(Long id) {
        try (Session session = getSession()) {
            return session.find(MaGiamGia.class, id);
        }
    }

    public MaGiamGia findByMaCode(String maCode) {
        try (Session session = getSession()) {
            return session.createQuery(
                            "FROM MaGiamGia WHERE maGiamGia = :ma", MaGiamGia.class)
                    .setParameter("ma", maCode)
                    .uniqueResult();
        }
    }

    public void saveOrUpdate(MaGiamGia mgg) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            if (mgg.getId() == null) {
                session.persist(mgg);
            } else {
                session.merge(mgg);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Lỗi lưu mã giảm giá", e);
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            MaGiamGia mgg = session.find(MaGiamGia.class, id);
            if (mgg != null) {
                session.remove(mgg);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Lỗi xóa mã giảm giá", e);
        }
    }
}
