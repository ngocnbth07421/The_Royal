package repository;

import entity.KhachHang;
import org.hibernate.Session;
import util.HibernateConfig;

import java.util.List;

public class KhachHangRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<KhachHang> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM KhachHang ORDER BY idKhachHang ASC", KhachHang.class).list();
        }
    }

    public KhachHang getOne(Long id) {
        try (Session session = getSession()) {
            return session.find(KhachHang.class, id);
        }
    }

    public KhachHang findByEmail(String email) {
        try (Session session = getSession()) {
            return session.createQuery("FROM KhachHang WHERE email = :email", KhachHang.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
    }

    public KhachHang findBySoDienThoai(String soDienThoai) {
        try (Session session = getSession()) {
            return session.createQuery("FROM KhachHang WHERE soDienThoai = :soDienThoai", KhachHang.class)
                    .setParameter("soDienThoai", soDienThoai)
                    .uniqueResult();
        }
    }

    public void add(KhachHang kh) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.save(kh);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(KhachHang kh) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.merge(kh);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
