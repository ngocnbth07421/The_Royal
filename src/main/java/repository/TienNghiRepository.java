package repository;

import entity.Phong;
import entity.TienNghi;
import org.hibernate.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Chỉ truy vấn DB (HQL). Không mở transaction ở đây - Service lo việc đó. */
public class TienNghiRepository {

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

    // ------------------------------------------------------------ KHO / PHÒNG (kiểm tra 2 chiều)

    /** id tiện nghi -> số phòng đang có tiện nghi đó. Tiện nghi chưa gán phòng nào sẽ không có trong map. */
    public Map<Long, Long> demPhongTheoTienNghi(Session s) {
        List<Object[]> rs = s.createQuery(
                "select t.id, count(p) from Phong p join p.tienNghis t group by t.id", Object[].class).getResultList();
        Map<Long, Long> kq = new HashMap<>();
        for (Object[] r : rs) kq.put(((Number) r[0]).longValue(), ((Number) r[1]).longValue());
        return kq;
    }

    public long demTongPhong(Session s) {
        return s.createQuery("select count(p) from Phong p", Long.class).getSingleResult();
    }

    public List<Phong> timPhongCoTienNghi(Session s, Long idTienNghi) {
        return s.createQuery("select p from Phong p join p.tienNghis t where t.id = :id order by p.maPhong", Phong.class)
                .setParameter("id", idTienNghi)
                .getResultList();
    }

    public List<Phong> timPhongChuaCoTienNghi(Session s, Long idTienNghi, String tuKhoa, int gioiHan) {
        boolean coLoc = tuKhoa != null && !tuKhoa.isBlank();
        String hql = "select p from Phong p where p.id not in "
                + "(select p2.id from Phong p2 join p2.tienNghis t where t.id = :id)"
                + (coLoc ? " and lower(p.maPhong) like :q" : "")
                + " order by p.maPhong";
        var q = s.createQuery(hql, Phong.class).setParameter("id", idTienNghi);
        if (coLoc) q.setParameter("q", "%" + tuKhoa.trim().toLowerCase() + "%");
        return q.setMaxResults(gioiHan).getResultList();
    }

    public List<Phong> timPhongTheoDanhSachId(Session s, List<Long> ids) {
        if (ids.isEmpty()) return List.of();
        return s.createQuery("select p from Phong p where p.id in (:ids)", Phong.class)
                .setParameter("ids", ids)
                .getResultList();
    }
}
