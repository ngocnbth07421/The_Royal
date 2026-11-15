package repository;

import entity.TienNghi;
import entity.TienNghiPhong;
import org.hibernate.Session;
import util.HibernateConfig;

import java.util.Collections;
import java.util.List;

public class TienNghiPhongRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<TienNghiPhong> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM TienNghiPhong", TienNghiPhong.class).list();
        }
    }

    public List<TienNghiPhong> getByPhong(Long idPhong) {
        try (Session session = getSession()) {
            return session.createQuery("FROM TienNghiPhong WHERE idPhong = :idPhong", TienNghiPhong.class)
                    .setParameter("idPhong", idPhong)
                    .list();
        }
    }

    public List<TienNghi> getTienNghiListByPhong(Long idPhong) {
        try (Session session = getSession()) {
            String hql = "SELECT tn FROM TienNghi tn WHERE tn.idTienNghi IN " +
                    "(SELECT tnp.idTienNghi FROM TienNghiPhong tnp WHERE tnp.idPhong = :idPhong)";
            return session.createQuery(hql, TienNghi.class)
                    .setParameter("idPhong", idPhong)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void add(TienNghiPhong tnp) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.save(tnp);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteByPhong(Long idPhong) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.createQuery("DELETE FROM TienNghiPhong WHERE idPhong = :idPhong")
                    .setParameter("idPhong", idPhong)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
