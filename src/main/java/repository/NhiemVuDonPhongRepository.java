package repository;

import entity.NhiemVuDonPhong;
import org.hibernate.Session;
import util.HibernateConfig;

import java.util.List;

public class NhiemVuDonPhongRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<NhiemVuDonPhong> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM NhiemVuDonPhong", NhiemVuDonPhong.class).list();
        }
    }

    public List<NhiemVuDonPhong> getByPhong(Long idPhong) {
        try (Session session = getSession()) {
            return session.createQuery("FROM NhiemVuDonPhong WHERE idPhong = :idPhong", NhiemVuDonPhong.class)
                    .setParameter("idPhong", idPhong)
                    .list();
        }
    }

    public NhiemVuDonPhong getOne(Long id) {
        try (Session session = getSession()) {
            return session.find(NhiemVuDonPhong.class, id);
        }
    }

    public void add(NhiemVuDonPhong nvdp) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.save(nvdp);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(NhiemVuDonPhong nvdp) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            session.merge(nvdp);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try (Session session = getSession()) {
            session.getTransaction().begin();
            NhiemVuDonPhong nvdp = session.find(NhiemVuDonPhong.class, id);
            if (nvdp != null) {
                session.delete(nvdp);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
