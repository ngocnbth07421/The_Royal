package repository;

import controller.NghiepVuException;
import entity.HangPhong;
import entity.LoaiGiuong;
import entity.LoaiPhong;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.math.BigDecimal;
import java.util.List;

public class LoaiPhongRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<LoaiPhong> findAll() {
        return search(null);
    }

    public List<LoaiPhong> search(String tuKhoa) {
        try (Session session = getSession()) {
            StringBuilder hql = new StringBuilder(
                    "SELECT lp FROM LoaiPhong lp JOIN FETCH lp.hangPhong hp JOIN FETCH lp.loaiGiuong lg WHERE 1 = 1");
            if (tuKhoa != null && !tuKhoa.isBlank()) {
                hql.append(" AND (LOWER(lp.ten) LIKE :kw OR LOWER(hp.moTa) LIKE :kw OR LOWER(lg.ten) LIKE :kw)");
            }
            hql.append(" ORDER BY lp.giaMoiDem ASC");
            var q = session.createQuery(hql.toString(), LoaiPhong.class);
            if (tuKhoa != null && !tuKhoa.isBlank()) {
                q.setParameter("kw", "%" + tuKhoa.trim().toLowerCase() + "%");
            }
            return q.list();
        }
    }

    public List<LoaiPhong> getAll() {
        return findAll();
    }

    public LoaiPhong findById(Long id) {
        try (Session session = getSession()) {
            List<LoaiPhong> rs = session.createQuery(
                            "SELECT lp FROM LoaiPhong lp JOIN FETCH lp.hangPhong JOIN FETCH lp.loaiGiuong WHERE lp.id = :id",
                            LoaiPhong.class)
                    .setParameter("id", id)
                    .list();
            return rs.isEmpty() ? null : rs.get(0);
        }
    }

    public LoaiPhong getOne(Long id) {
        return findById(id);
    }

    public void add(LoaiPhong lp) { saveOrUpdate(lp); }
    public void update(LoaiPhong lp) { saveOrUpdate(lp); }

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
            Long countPhong = session.createQuery(
                            "SELECT COUNT(p) FROM Phong p WHERE p.loaiPhong.id = :id", Long.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (countPhong != null && countPhong > 0) {
                throw new NghiepVuException("Không thể xóa: vẫn còn " + countPhong + " phòng thuộc loại phòng này.");
            }
            LoaiPhong lp = session.find(LoaiPhong.class, id);
            if (lp != null) {
                session.remove(lp);
            }
            transaction.commit();
        } catch (NghiepVuException e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new NghiepVuException("Không thể xóa loại phòng đang được sử dụng.");
        }
    }

    public List<LoaiPhong> layDanhSach() { return findAll(); }
    public List<LoaiPhong> layDanhSach(String tuKhoa) { return search(tuKhoa); }
    public LoaiPhong timTheoId(Long id) { return findById(id); }
    public List<HangPhong> layDanhSachHangPhong() { return findAllHangPhong(); }
    public List<LoaiGiuong> layDanhSachLoaiGiuong() { return findAllLoaiGiuong(); }

    public void luuLoaiPhong(Long id, String ten, BigDecimal giaMoiDem, String moTa, Long idHangPhong, Long idLoaiGiuong) {
        if (ten == null || ten.trim().isEmpty()) {
            throw new NghiepVuException("Tên loại phòng không được để trống.");
        }
        if (idHangPhong == null || idLoaiGiuong == null) {
            throw new NghiepVuException("Vui lòng chọn hạng phòng và loại giường.");
        }
        if (giaMoiDem == null || giaMoiDem.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NghiepVuException("Giá mỗi đêm bắt buộc phải lớn hơn 0.");
        }

        LoaiPhong trung = findByHangVaGiuong(idHangPhong, idLoaiGiuong);
        if (trung != null && (id == null || !trung.getId().equals(id))) {
            throw new NghiepVuException("Tổ hợp Hạng phòng và Loại giường này đã tồn tại ở loại phòng: " + trung.getTen());
        }

        LoaiPhong lp = (id != null) ? findById(id) : new LoaiPhong();
        if (lp == null) lp = new LoaiPhong();

        HangPhong hp = new HangPhong();
        hp.setId(idHangPhong);
        LoaiGiuong lg = new LoaiGiuong();
        lg.setId(idLoaiGiuong);

        lp.setTen(ten.trim());
        lp.setGiaMoiDem(giaMoiDem);
        lp.setMoTa(moTa != null ? moTa.trim() : null);
        lp.setHangPhong(hp);
        lp.setLoaiGiuong(lg);

        saveOrUpdate(lp);
    }

    public void xoaLoaiPhong(Long id) {
        delete(id);
    }
}
