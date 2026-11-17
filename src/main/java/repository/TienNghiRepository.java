package repository;

import entity.TienNghi;
import org.hibernate.Session;

import java.util.List;

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
}
