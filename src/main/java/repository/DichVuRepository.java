package repository;

import controller.NghiepVuException;
import entity.DichVu;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.List;

public class DichVuRepository {

    public List<DichVu> getAll() {
        return search(null, null);
    }

    public List<DichVu> search(String tuKhoa, String trangThai) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            StringBuilder hql = new StringBuilder("FROM DichVu d WHERE 1 = 1");
            if (tuKhoa != null && !tuKhoa.isBlank()) {
                hql.append(" AND (LOWER(d.ma_dich_vu) LIKE :kw OR LOWER(d.ten_dich_vu) LIKE :kw OR LOWER(d.danh_muc) LIKE :kw)");
            }
            if (trangThai != null && !trangThai.isBlank()) {
                if ("Hoạt động".equals(trangThai) || "HOAT_DONG".equals(trangThai)) {
                    hql.append(" AND d.trang_thai IN (N'Hoạt động', 'HOAT_DONG')");
                } else {
                    hql.append(" AND d.trang_thai IN (N'Ngưng', 'NGUNG')");
                }
            }
            hql.append(" ORDER BY d.id_dich_vu DESC");
            var q = session.createQuery(hql.toString(), DichVu.class);
            if (tuKhoa != null && !tuKhoa.isBlank()) {
                q.setParameter("kw", "%" + tuKhoa.trim().toLowerCase() + "%");
            }
            return q.getResultList();
        }
    }

    public DichVu getOne(Long id) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.find(DichVu.class, id);
        }
    }

    public void add(DichVu dv) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            session.persist(dv);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    public void update(DichVu dv) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            session.merge(dv);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Long countUsed = session.createQuery(
                            "SELECT COUNT(s) FROM SuDungDichVu s WHERE s.dichVu.id_dich_vu = :id", Long.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (countUsed != null && countUsed > 0) {
                throw new NghiepVuException("Không thể xóa dịch vụ đang có " + countUsed + " lượt sử dụng. Hãy chuyển sang trạng thái 'Ngưng'.");
            }
            DichVu dv = session.find(DichVu.class, id);
            if (dv != null) {
                session.remove(dv);
            }
            transaction.commit();
        } catch (NghiepVuException e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        }
    }
}
