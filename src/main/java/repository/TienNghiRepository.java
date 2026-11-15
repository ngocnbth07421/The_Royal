package repository;

import entity.TienNghi;
import org.hibernate.Session;
import util.HibernateConfig;

import java.util.List;

public class TienNghiRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<TienNghi> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM TienNghi", TienNghi.class).list();
        }
    }

    public TienNghi getOne(Long id) {
        try (Session session = getSession()) {
            return session.find(TienNghi.class, id);
        }
    }
}
