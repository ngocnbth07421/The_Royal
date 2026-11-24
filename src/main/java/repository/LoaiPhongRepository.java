package repository;

import entity.HangPhong;
import entity.LoaiGiuong;
import entity.LoaiPhong;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.List;

public class LoaiPhongRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<LoaiPhong> findAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM LoaiPhong ORDER BY giaMoiDem ASC", LoaiPhong.class).list();
        }
    }

    public List<LoaiPhong> getAll() {
        return findAll();
    }

    public LoaiPhong findById(Long id) {
        try (Session session = getSession()) {
            return session.find(LoaiPhong.class, id);
        }
    }

    public LoaiPhong getOne(Long id) {
        return findById(id);
    }

    public void add(LoaiPhong lp) {
        saveOrUpdate(lp);
    }

    public void update(LoaiPhong lp) {
        saveOrUpdate(lp);
    }

    public List<HangPhong> findAllHangPhong() {
        try (Session session = getSession()) {
            return session.createQuery("FROM HangPhong ORDER BY id", HangPhong.class).list();
        }
    }

    public List<LoaiGiuong> findAllLoaiGiuong() {
        try (Session session = getSession()) {
            return session.createQuery("FROM LoaiGiuong ORDER BY id", LoaiGiuong.class).list();
        }
    }

    public LoaiPhong findByHangVaGiuong(Long idHangPhong, Long idLoaiGiuong) {
        try (Session session = getSession()) {
            return session.createQuery(
                            "FROM LoaiPhong WHERE hangPhong.id = :hp AND loaiGiuong.id = :lg", LoaiPhong.class)
                    .setParameter("hp", idHangPhong)
                    .setParameter("lg", idLoaiGiuong)
                    .uniqueResult();
        }
    }

    public void saveOrUpdate(LoaiPhong lp) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            if (lp.getId() == null) {
                session.persist(lp);
            } else {
                session.merge(lp);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Lỗi lưu loại phòng", e);
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            LoaiPhong lp = session.find(LoaiPhong.class, id);
            if (lp != null) {
                session.remove(lp);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Lỗi xóa loại phòng", e);
        }
    }
}
