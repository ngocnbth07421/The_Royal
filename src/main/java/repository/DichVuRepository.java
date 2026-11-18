
package repository;

import entity.DichVu;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.List;

public class DichVuRepository {

    // LAY TAT CA DICH VU
    public List<DichVu> getAll() {

        try (Session session =
                     HibernateConfig.getFACTORY().openSession()) {

            return session.createQuery(
                    "FROM DichVu",
                    DichVu.class
            ).getResultList();
        }
    }

    // LAY DICH VU THEO ID
    public DichVu getOne(Long id) {

        try (Session session =
                     HibernateConfig.getFACTORY().openSession()) {

            return session.find(DichVu.class, id);
        }
    }

    // THEM DICH VU
    public void add(DichVu dv) {

        Transaction transaction = null;

        try (Session session =
                     HibernateConfig.getFACTORY().openSession()) {

            transaction = session.beginTransaction();

            session.persist(dv);

            transaction.commit();

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    // CAP NHAT DICH VU
    public void update(DichVu dv) {

        Transaction transaction = null;

        try (Session session =
                     HibernateConfig.getFACTORY().openSession()) {

            transaction = session.beginTransaction();

            session.merge(dv);

            transaction.commit();

        } catch (RuntimeException e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw e;
        }
    }

    // XOA DICH VU
    public void delete(Long id) {

        Transaction transaction = null;

        try (Session session =
                     HibernateConfig.getFACTORY().openSession()) {

            transaction = session.beginTransaction();

            DichVu dv = session.find(DichVu.class, id);

            if (dv != null) {
                session.remove(dv);
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

