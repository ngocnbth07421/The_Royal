package repository;

import entity.MaGiamGia;
import controller.NghiepVuException;
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
        try (Session session = getSession()) {
            return session.createQuery("FROM MaGiamGia ORDER BY id DESC", MaGiamGia.class).list();
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

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            MaGiamGia mgg = session.find(MaGiamGia.class, id);
            if (mgg != null) {
                session.remove(mgg);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Lỗi xóa mã giảm giá", e);
        }
    }

    public List<MaGiamGia> layDanhSach() { return findAll(); }
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

        if ("PHAN_TRAM".equals(mgg.getLoaiGiamGia())) {
            if (mgg.getGiaTri().compareTo(BigDecimal.ONE) < 0 || mgg.getGiaTri().compareTo(new BigDecimal("50")) > 0) {
                throw new NghiepVuException("Mức giảm giá theo phần trăm chỉ cho phép tối thiểu 1% và tối đa 50%.");
            }
        }

        if (mgg.getBatDauLuc() == null || mgg.getKetThucLuc() == null
                || !mgg.getKetThucLuc().isAfter(mgg.getBatDauLuc())) {
            throw new NghiepVuException("Thời điểm kết thúc bắt buộc phải lớn hơn thời điểm bắt đầu.");
        }
        if (mgg.getSoLuotDaDung() == null || mgg.getSoLuotDaDung() < 0) {
            mgg.setSoLuotDaDung(0);
        }

        mgg.setMaGiamGia(code);
        saveOrUpdate(mgg);
    }

    public void xoaMaGiamGia(Long id) {
        delete(id);
    }
}
