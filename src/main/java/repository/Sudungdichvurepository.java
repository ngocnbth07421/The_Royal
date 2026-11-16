
package repository;

import entity.sudungdichvu;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.List;

public class Sudungdichvurepository {

    // LAY TAT CA SU DUNG DICH VU
    public List<sudungdichvu> getAll() {

        try (Session session =
                     HibernateConfig.getFACTORY().openSession()) {

            return session.createQuery(
                    "FROM sudungdichvu",
                    sudungdichvu.class
            ).getResultList();
        }
    }

    // LAY SU DUNG DICH VU THEO ID
    public sudungdichvu getOne(Long id) {

        try (Session session =
                     HibernateConfig.getFACTORY().openSession()) {

            return session.find(sudungdichvu.class, id);
        }
    }

    // THEM SU DUNG DICH VU
    public void add(sudungdichvu sddv) {

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
    public void update(sudungdichvu sddv) {

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

            sudungdichvu sddv =
                    session.find(sudungdichvu.class, id);

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
