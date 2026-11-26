package repository;

import entity.DanhGia;
import entity.DatPhong;
import entity.KhachHang;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.List;

public class DanhGiaRepository {

    public List<DanhGia> getAll() {
        return search(null, null);
    }

    public List<DanhGia> search(String tuKhoa, Integer diem) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            StringBuilder hql = new StringBuilder(
                    "SELECT d FROM DanhGia d LEFT JOIN FETCH d.datPhong dp LEFT JOIN FETCH d.khachHang kh WHERE 1 = 1");
            if (tuKhoa != null && !tuKhoa.isBlank()) {
                hql.append(" AND (LOWER(dp.maDatPhong) LIKE :kw OR LOWER(kh.hoTen) LIKE :kw OR LOWER(kh.email) LIKE :kw OR LOWER(d.binhLuan) LIKE :kw)");
            }
            if (diem != null) {
                hql.append(" AND d.diemDanhGia = :diem");
            }
            hql.append(" ORDER BY d.idDanhGia DESC");
            var q = session.createQuery(hql.toString(), DanhGia.class);
            if (tuKhoa != null && !tuKhoa.isBlank()) {
                q.setParameter("kw", "%" + tuKhoa.trim().toLowerCase() + "%");
            }
            if (diem != null) {
                q.setParameter("diem", diem);
            }
            return q.list();
        }
    }

    public DanhGia getOne(Integer id) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            List<DanhGia> rs = session.createQuery(
                            "SELECT d FROM DanhGia d LEFT JOIN FETCH d.datPhong LEFT JOIN FETCH d.khachHang WHERE d.idDanhGia = :id",
                            DanhGia.class)
                    .setParameter("id", id)
                    .list();
            return rs.isEmpty() ? null : rs.get(0);
        }
    }

    public void add(DanhGia dg) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            tx = session.beginTransaction();
            session.persist(dg);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Lỗi thêm đánh giá", e);
        }
    }

    public void update(DanhGia dg) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            tx = session.beginTransaction();
            session.merge(dg);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Lỗi cập nhật đánh giá", e);
        }
    }

    public void delete(Integer id) {
        Transaction tx = null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            tx = session.beginTransaction();
            DanhGia dg = session.find(DanhGia.class, id);
            if (dg != null) {
                session.remove(dg);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Lỗi xóa đánh giá", e);
        }
    }

    public List<DatPhong> getAllDatPhong() {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery("FROM DatPhong ORDER BY id DESC", DatPhong.class).list();
        }
    }

    public List<KhachHang> getAllKhachHang() {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery("FROM KhachHang ORDER BY id ASC", KhachHang.class).list();
        }
    }
}
