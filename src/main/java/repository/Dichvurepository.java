
package repository;

import entity.dichvu;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.List;

public class Dichvurepository {

    // LAY TAT CA DICH VU
    public List<dichvu> getAll() {

        try (Session session =
                     HibernateConfig.getFACTORY().openSession()) {

            return session.createQuery(
                    "FROM dichvu",
                    dichvu.class
            ).getResultList();
        }
    }

    // LAY DICH VU THEO ID
    public dichvu getOne(Long id) {

        try (Session session =
                     HibernateConfig.getFACTORY().openSession()) {

            return session.find(dichvu.class, id);
        }
    }

    // THEM DICH VU
    public void add(dichvu dv) {

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
    public void update(dichvu dv) {

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

            dichvu dv = session.find(dichvu.class, id);

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
