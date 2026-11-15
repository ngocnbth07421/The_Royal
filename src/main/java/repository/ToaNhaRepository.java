package repository;

import entity.ToaNha;
import org.hibernate.Session;
import util.HibernateConfig;

import java.util.List;

public class ToaNhaRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<ToaNha> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM ToaNha", ToaNha.class).list();
        }
    }

    public ToaNha getOne(Long id) {
        try (Session session = getSession()) {
            return session.find(ToaNha.class, id);
        }
    }

    public void add(ToaNha toaNha) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.save(toaNha);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(ToaNha toaNha) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.merge(toaNha);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            ToaNha tn = session.find(ToaNha.class, id);
            if (tn != null) {
                session.delete(tn);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
