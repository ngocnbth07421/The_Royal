
package repository;

import entity.SuDungDichVu;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.List;

public class SuDungDichVuRepository {

    // LAY TAT CA SU DUNG DICH VU
    public List<SuDungDichVu> getAll() {

        try (Session session =
                     HibernateConfig.getFACTORY().openSession()) {

            return session.createQuery(
                    "FROM SuDungDichVu",
                    SuDungDichVu.class
            ).getResultList();
        }
    }

    // LAY SU DUNG DICH VU THEO ID
    public SuDungDichVu getOne(Long id) {

        try (Session session =
                     HibernateConfig.getFACTORY().openSession()) {

            return session.find(SuDungDichVu.class, id);
        }
    }

    // THEM SU DUNG DICH VU
    public void add(SuDungDichVu sddv) {

        Transaction transaction = null;

        try (Session session =
                     HibernateConfig.getFACTORY().openSession()) {

            transaction = session.beginTransaction();

            session.persist(sddv);

            transaction.commit();

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    // CAP NHAT SU DUNG DICH VU
    public void update(SuDungDichVu sddv) {

        Transaction transaction = null;

        try (Session session =
                     HibernateConfig.getFACTORY().openSession()) {

            transaction = session.beginTransaction();

            session.merge(sddv);

            transaction.commit();

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    // XOA SU DUNG DICH VU
    public void delete(Long id) {

        Transaction transaction = null;

        try (Session session =
                     HibernateConfig.getFACTORY().openSession()) {

            transaction = session.beginTransaction();

            SuDungDichVu sddv =
                    session.find(SuDungDichVu.class, id);

            if (sddv != null) {
                session.remove(sddv);
            }

            transaction.commit();

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }
}

