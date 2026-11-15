package repository;

import entity.NhanVien;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateConfig;

import java.util.List;

public class NhanVienRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<NhanVien> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM NhanVien ORDER BY idNhanVien ASC", NhanVien.class).list();
        }
    }

    public List<NhanVien> getPage(int page, int pageSize) {
        try (Session session = getSession()) {
            Query<NhanVien> query = session.createQuery("FROM NhanVien ORDER BY idNhanVien ASC", NhanVien.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.list();
        }
    }

    public long count() {
        try (Session session = getSession()) {
            return session.createQuery("SELECT COUNT(nv.idNhanVien) FROM NhanVien nv", Long.class).uniqueResult();
        }
    }

    public NhanVien getOne(Long id) {
        try (Session session = getSession()) {
            return session.find(NhanVien.class, id);
        }
    }

    public NhanVien findByMaNhanVien(String maNhanVien) {
        try (Session session = getSession()) {
            return session.createQuery("FROM NhanVien WHERE maNhanVien = :maNhanVien", NhanVien.class)
                    .setParameter("maNhanVien", maNhanVien)
                    .uniqueResult();
        }
    }

    public NhanVien findBySoDienThoai(String soDienThoai) {
        try (Session session = getSession()) {
            return session.createQuery("FROM NhanVien WHERE soDienThoai = :soDienThoai", NhanVien.class)
                    .setParameter("soDienThoai", soDienThoai)
                    .uniqueResult();
        }
    }

    public void add(NhanVien nhanVien) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.save(nhanVien);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(NhanVien nhanVien) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.merge(nhanVien);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            NhanVien nv = session.find(NhanVien.class, id);
            if (nv != null) {
                session.delete(nv);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
