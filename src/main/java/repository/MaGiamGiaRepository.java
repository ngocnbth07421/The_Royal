package repository;

import controller.NghiepVuException;
import entity.MaGiamGia;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.math.BigDecimal;
import java.util.List;

public class MaGiamGiaRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<MaGiamGia> findAll() {
        return search(null, null);
    }

    public List<MaGiamGia> search(String tuKhoa, String trangThai) {
        try (Session session = getSession()) {
            StringBuilder hql = new StringBuilder("FROM MaGiamGia m WHERE 1 = 1");
            if (tuKhoa != null && !tuKhoa.isBlank()) {
                hql.append(" AND LOWER(m.maGiamGia) LIKE :kw");
            }
            if (trangThai != null && !trangThai.isBlank()) {
                if ("Hoạt động".equals(trangThai) || "HOAT_DONG".equals(trangThai)) {
                    hql.append(" AND m.trangThai IN (N'Hoạt động', 'HOAT_DONG')");
                } else {
                    hql.append(" AND m.trangThai IN (N'Ngưng', 'NGUNG')");
                }
            }
            hql.append(" ORDER BY m.id DESC");
            var q = session.createQuery(hql.toString(), MaGiamGia.class);
            if (tuKhoa != null && !tuKhoa.isBlank()) {
                q.setParameter("kw", "%" + tuKhoa.trim().toLowerCase() + "%");
            }
            return q.list();
        }
    }

    public MaGiamGia findById(Long id) {
        try (Session session = getSession()) {
            return session.find(MaGiamGia.class, id);
        }
    }

    public MaGiamGia findByMaCode(String maCode) {
        try (Session session = getSession()) {
            return session.createQuery(
                            "FROM MaGiamGia WHERE maGiamGia = :ma", MaGiamGia.class)
                    .setParameter("ma", maCode)
                    .uniqueResult();
        }
    }

    public void saveOrUpdate(MaGiamGia mgg) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            if (mgg.getId() == null) {
                session.persist(mgg);
            } else {
                session.merge(mgg);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Lỗi lưu mã giảm giá", e);
        }
    }

    public void doiTrangThai(Long id) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            MaGiamGia mgg = session.find(MaGiamGia.class, id);
            if (mgg == null) throw new NghiepVuException("Không tìm thấy mã giảm giá.");
            boolean dangHoatDong = "Hoạt động".equals(mgg.getTrangThai()) || "HOAT_DONG".equals(mgg.getTrangThai());
            mgg.setTrangThai(dangHoatDong ? MaGiamGia.NGUNG : MaGiamGia.HOAT_DONG);
            session.merge(mgg);
            transaction.commit();
        } catch (NghiepVuException e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Lỗi đổi trạng thái mã giảm giá", e);
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            Long countUsed = session.createQuery(
                            "SELECT COUNT(d) FROM DatPhong d WHERE d.maGiamGia.id = :id", Long.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (countUsed != null && countUsed > 0) {
                throw new NghiepVuException("Không thể xóa mã giảm giá đã áp dụng cho " + countUsed + " đơn đặt phòng. Hãy chuyển sang 'Ngưng'.");
            }
            MaGiamGia mgg = session.find(MaGiamGia.class, id);
            if (mgg != null) {
                session.remove(mgg);
            }
            transaction.commit();
        } catch (NghiepVuException e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new NghiepVuException("Không thể xóa mã giảm giá đang được sử dụng.");
        }
    }

    public List<MaGiamGia> layDanhSach() { return findAll(); }
    public List<MaGiamGia> layDanhSach(String tuKhoa, String trangThai) { return search(tuKhoa, trangThai); }
    public MaGiamGia timTheoId(Long id) { return findById(id); }

    public void luuMaGiamGia(MaGiamGia mgg) {
        if (mgg.getMaGiamGia() == null || mgg.getMaGiamGia().trim().isEmpty()) {
            throw new NghiepVuException("Mã giảm giá không được để trống.");
        }
        String code = mgg.getMaGiamGia().trim().toUpperCase();
        MaGiamGia trung = findByMaCode(code);
        if (trung != null && (mgg.getId() == null || !trung.getId().equals(mgg.getId()))) {
            throw new NghiepVuException("Mã giảm giá '" + code + "' đã tồn tại trên hệ thống.");
        }

        if (mgg.getGiaTri() == null || mgg.getGiaTri().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NghiepVuException("Giá trị giảm phải lớn hơn 0.");
        }

        if ("PHAN_TRAM".equals(mgg.getLoaiGiamGia()) || "Phần trăm".equals(mgg.getLoaiGiamGia())) {
            if (mgg.getGiaTri().compareTo(BigDecimal.ONE) < 0 || mgg.getGiaTri().compareTo(new BigDecimal("50")) > 0) {
                throw new NghiepVuException("Mức giảm giá theo phần trăm chỉ cho phép tối thiểu 1% và tối đa 50%.");
            }
        }

        if (mgg.getBatDauLuc() == null || mgg.getKetThucLuc() == null
                || !mgg.getKetThucLuc().isAfter(mgg.getBatDauLuc())) {
            throw new NghiepVuException("Thời điểm kết thúc bắt buộc phải lớn hơn thời điểm bắt đầu.");
        }
        if (mgg.getSoLuotConDung() == null || mgg.getSoLuotConDung() < 0) {
            mgg.setSoLuotConDung(0);
        }

        mgg.setMaGiamGia(code);
        saveOrUpdate(mgg);
    }

    public void xoaMaGiamGia(Long id) {
        delete(id);
    }
}
