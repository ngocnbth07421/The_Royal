package repository;

import entity.NguoiDung;
import org.hibernate.Session;
import util.HibernateConfig;

import java.util.List;

public class NguoiDungRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<NguoiDung> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM NguoiDung ORDER BY idNguoiDung ASC", NguoiDung.class).list();
        }
    }

    public NguoiDung getOne(Long id) {
        try (Session session = getSession()) {
            return session.find(NguoiDung.class, id);
        }
    }

    public NguoiDung findByEmail(String email) {
        try (Session session = getSession()) {
            return session.createQuery("FROM NguoiDung WHERE email = :email", NguoiDung.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
    }

    public NguoiDung findByNhanVien(Long idNhanVien) {
        try (Session session = getSession()) {
            return session.createQuery("FROM NguoiDung WHERE idNhanVien = :idNhanVien", NguoiDung.class)
                    .setParameter("idNhanVien", idNhanVien)
                    .uniqueResult();
        }
    }

    public void add(NguoiDung nd) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.save(nd);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(NguoiDung nd) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.merge(nd);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            NguoiDung nd = session.find(NguoiDung.class, id);
            if (nd != null) {
                session.delete(nd);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
