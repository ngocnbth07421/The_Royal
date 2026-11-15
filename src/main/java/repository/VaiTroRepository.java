package repository;

import entity.VaiTro;
import org.hibernate.Session;
import util.HibernateConfig;

import java.util.List;

public class VaiTroRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<VaiTro> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM VaiTro", VaiTro.class).list();
        }
    }

    public VaiTro getOne(Long id) {
        try (Session session = getSession()) {
            return session.find(VaiTro.class, id);
        }
    }

    public void add(VaiTro vaiTro) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.save(vaiTro);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(VaiTro vaiTro) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.merge(vaiTro);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            VaiTro vt = session.find(VaiTro.class, id);
            if (vt != null) {
                session.delete(vt);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
