package repository;

import entity.Tang;
import org.hibernate.Session;
import util.HibernateConfig;

import java.util.List;

public class TangRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<Tang> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM Tang ORDER BY soTang ASC", Tang.class).list();
        }
    }

    public List<Tang> getByToaNha(Long idToaNha) {
        try (Session session = getSession()) {
            return session.createQuery("FROM Tang WHERE idToaNha = :idToaNha ORDER BY soTang ASC", Tang.class)
                    .setParameter("idToaNha", idToaNha)
                    .list();
        }
    }

    public Tang getOne(Long id) {
        try (Session session = getSession()) {
            return session.find(Tang.class, id);
        }
    }

    public void add(Tang tang) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.save(tang);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Tang tang) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.merge(tang);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            Tang t = session.find(Tang.class, id);
            if (t != null) {
                session.delete(t);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
