package repository;

import entity.SuDungDichVu;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.List;

public class SuDungDichVuRepository {

    public List<SuDungDichVu> getAll() {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery(
                    "SELECT s FROM SuDungDichVu s LEFT JOIN FETCH s.dichVu LEFT JOIN FETCH s.datPhongChiTiet dpc LEFT JOIN FETCH dpc.phong LEFT JOIN FETCH dpc.datPhong ORDER BY s.id_su_dung_dich_vu DESC",
                    SuDungDichVu.class
            ).getResultList();
        }
    }

    public SuDungDichVu getOne(Long id) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.find(SuDungDichVu.class, id);
        }
    }

    public void add(SuDungDichVu sddv) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            session.persist(sddv);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    public void update(SuDungDichVu sddv) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            session.merge(sddv);
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
            SuDungDichVu sddv = session.find(SuDungDichVu.class, id);
            if (sddv != null) {
                session.remove(sddv);
            }
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        }
    }
}
