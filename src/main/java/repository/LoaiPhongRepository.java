package repository;

import entity.LoaiPhong;
import org.hibernate.Session;
import util.HibernateConfig;

import java.util.List;

public class LoaiPhongRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<LoaiPhong> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM LoaiPhong ORDER BY giaMoiDem ASC", LoaiPhong.class).list();
        }
    }

    public LoaiPhong getOne(Long id) {
        try (Session session = getSession()) {
            return session.find(LoaiPhong.class, id);
        }
    }

    public void add(LoaiPhong loaiPhong) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.save(loaiPhong);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(LoaiPhong loaiPhong) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.merge(loaiPhong);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            LoaiPhong lp = session.find(LoaiPhong.class, id);
            if (lp != null) {
                session.delete(lp);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
