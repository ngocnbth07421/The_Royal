package repository;

import entity.Phong;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateConfig;

import java.util.List;

public class PhongRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<Phong> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM Phong ORDER BY idPhong ASC", Phong.class).list();
        }
    }

    public List<Phong> getPage(int page, int pageSize) {
        try (Session session = getSession()) {
            Query<Phong> query = session.createQuery("FROM Phong ORDER BY idPhong ASC", Phong.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.list();
        }
    }

    public long count() {
        try (Session session = getSession()) {
            return session.createQuery("SELECT COUNT(p.idPhong) FROM Phong p", Long.class).uniqueResult();
        }
    }

    public Phong getOne(Long id) {
        try (Session session = getSession()) {
            return session.find(Phong.class, id);
        }
    }

    public Phong findByMaPhong(String maPhong) {
        try (Session session = getSession()) {
            return session.createQuery("FROM Phong WHERE maPhong = :maPhong", Phong.class)
                    .setParameter("maPhong", maPhong)
                    .uniqueResult();
        }
    }

    public void add(Phong phong) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.save(phong);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Phong phong) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.merge(phong);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            Phong p = session.find(Phong.class, id);
            if (p != null) {
                session.delete(p);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
