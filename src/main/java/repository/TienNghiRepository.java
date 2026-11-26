package repository;

import entity.Phong;
import entity.TienNghi;
import controller.NghiepVuException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.*;
import java.util.stream.Collectors;

public class TienNghiRepository {

    public List<TienNghi> danhSach(String tuKhoa) {
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            return timTatCa(s, tuKhoa);
        }
    }

    public TienNghi layTheoId(Long id) {
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            TienNghi t = timTheoId(s, id);
            if (t == null) throw new NghiepVuException("Không tìm thấy tiện nghi.");
            return t;
        }
    }

    public TienNghi them(String ten, Integer soLuong) {
        String tenSach = kiemTraDauVao(ten, soLuong);
        Transaction tx = null;
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            tx = s.beginTransaction();
            if (tonTaiTen(s, tenSach, null))
                throw new NghiepVuException("Tiện nghi '" + tenSach + "' đã tồn tại.");
            TienNghi t = new TienNghi(tenSach, soLuong);
            them(s, t);
            tx.commit();
            return t;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            if (e instanceof NghiepVuException) throw (NghiepVuException) e;
            throw new RuntimeException("Lỗi thêm tiện nghi", e);
        }
    }

    public TienNghi capNhat(Long id, String ten, Integer soLuong) {
        String tenSach = kiemTraDauVao(ten, soLuong);
        Transaction tx = null;
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            tx = s.beginTransaction();
            TienNghi t = timTheoId(s, id);
            if (t == null) throw new NghiepVuException("Không tìm thấy tiện nghi.");
            if (tonTaiTen(s, tenSach, id))
                throw new NghiepVuException("Tiện nghi '" + tenSach + "' đã tồn tại.");
            t.setTenTienNghi(tenSach);
            t.setSoLuong(soLuong);
            s.merge(t);
            tx.commit();
            return t;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            if (e instanceof NghiepVuException) throw (NghiepVuException) e;
            throw new RuntimeException("Lỗi cập nhật tiện nghi", e);
        }
    }

    public void xoa(Long id) {
        Transaction tx = null;
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            tx = s.beginTransaction();
            TienNghi t = timTheoId(s, id);
            if (t == null) throw new NghiepVuException("Tiện nghi không tồn tại (có thể đã bị xóa).");
            long soPhong = demPhongDangDung(s, id);
            if (soPhong > 0)
                throw new NghiepVuException("Không thể xóa: tiện nghi '" + t.getTenTienNghi()
                        + "' đang được gắn cho " + soPhong + " phòng. Hãy gỡ khỏi các phòng trước.");
            xoa(s, t);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            if (e instanceof NghiepVuException) throw (NghiepVuException) e;
            throw new RuntimeException("Lỗi xóa tiện nghi", e);
        }
    }

    public List<Phong> danhSachPhong() {
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            return s.createQuery("FROM Phong ORDER BY maPhong ASC", Phong.class).getResultList();
        }
    }

    public Phong layPhong(Long idPhong) {
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            Phong p = s.find(Phong.class, idPhong);
            if (p == null) throw new NghiepVuException("Không tìm thấy phòng.");
            return p;
        }
    }

    public List<TienNghi> tienNghiCuaPhong(Long idPhong) {
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            Phong p = s.find(Phong.class, idPhong);
            if (p == null) throw new NghiepVuException("Không tìm thấy phòng.");
            return p.getTienNghis().stream()
                    .sorted(Comparator.comparing(TienNghi::getTenTienNghi))
                    .collect(Collectors.toList());
        }
    }

    public void ganChoPhong(Long idPhong, Collection<Long> idTienNghi) {
        Set<Long> muonCo = new HashSet<>(idTienNghi);
        Transaction tx = null;
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            tx = s.beginTransaction();
            Phong p = s.find(Phong.class, idPhong);
            if (p == null) throw new NghiepVuException("Không tìm thấy phòng.");
            List<TienNghi> moi = timTheoDanhSachId(s, new ArrayList<>(muonCo));
            if (moi.size() != muonCo.size())
                throw new NghiepVuException("Có tiện nghi không tồn tại trong danh sách đã chọn.");

            p.getTienNghis().removeIf(t -> !muonCo.contains(t.getId()));
            Set<Long> daCo = p.getTienNghis().stream().map(TienNghi::getId).collect(Collectors.toSet());
            for (TienNghi t : moi) if (!daCo.contains(t.getId())) p.getTienNghis().add(t);
            s.merge(p);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            if (e instanceof NghiepVuException) throw (NghiepVuException) e;
            throw new RuntimeException("Lỗi gán tiện nghi cho phòng", e);
        }
    }

    private String kiemTraDauVao(String ten, Integer soLuong) {
        if (ten == null || ten.isBlank()) throw new NghiepVuException("Tên tiện nghi không được để trống.");
        String sach = ten.trim().replaceAll("\\s+", " ");
        if (sach.length() > 100) throw new NghiepVuException("Tên tiện nghi tối đa 100 ký tự.");
        if (soLuong == null || soLuong < 0) throw new NghiepVuException("Số lượng phải là số nguyên >= 0.");
        return sach;
    }

    public List<TienNghi> timTatCa(Session s, String tuKhoa) {
        boolean coLoc = tuKhoa != null && !tuKhoa.isBlank();
        String hql = "from TienNghi t" + (coLoc ? " where lower(t.tenTienNghi) like :kw" : "")
                + " order by t.tenTienNghi";
        var q = s.createQuery(hql, TienNghi.class);
        if (coLoc) q.setParameter("kw", "%" + tuKhoa.trim().toLowerCase() + "%");
        return q.getResultList();
    }

    public TienNghi timTheoId(Session s, Long id) {
        return s.find(TienNghi.class, id);
    }

    /** Kiểm tra trùng tên (không phân biệt hoa thường). boQuaId dùng khi cập nhật để không tự trùng chính nó. */
    public boolean tonTaiTen(Session s, String ten, Long boQuaId) {
        String hql = "select count(t) from TienNghi t where lower(t.tenTienNghi) = :ten"
                + (boQuaId != null ? " and t.id <> :id" : "");
        var q = s.createQuery(hql, Long.class).setParameter("ten", ten.trim().toLowerCase());
        if (boQuaId != null) q.setParameter("id", boQuaId);
        return q.getSingleResult() > 0;
    }

    /** Số phòng đang gắn tiện nghi này (bảng tien_nghi_phong). */
    public long demPhongDangDung(Session s, Long idTienNghi) {
        return s.createQuery("select count(p) from Phong p join p.tienNghis t where t.id = :id", Long.class)
                .setParameter("id", idTienNghi)
                .getSingleResult();
    }

    public void them(Session s, TienNghi t) { s.persist(t); }

    public void xoa(Session s, TienNghi t) { s.remove(t); }

    public List<TienNghi> timTheoDanhSachId(Session s, List<Long> ids) {
        if (ids.isEmpty()) return List.of();
        return s.createQuery("from TienNghi t where t.id in :ids", TienNghi.class)
                .setParameter("ids", ids)
                .getResultList();
    }
}
