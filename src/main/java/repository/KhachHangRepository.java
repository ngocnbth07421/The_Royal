package repository;

import controller.NghiepVuException;
import entity.DatPhong;
import entity.KhachHang;
import entity.TheThanhVien;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.util.List;

public class KhachHangRepository {

    public List<KhachHang> getAll() {
        return search(null, null);
    }

    public List<KhachHang> search(String tuKhoa, String trangThai) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            StringBuilder hql = new StringBuilder(
                    "SELECT k FROM KhachHang k LEFT JOIN FETCH k.nguoiDung nd WHERE 1 = 1");
            if (tuKhoa != null && !tuKhoa.isBlank()) {
                hql.append(" AND (LOWER(k.hoTen) LIKE :kw OR k.soDienThoai LIKE :kw OR LOWER(k.email) LIKE :kw)");
            }
            if (trangThai != null && !trangThai.isBlank()) {
                if ("BI_KHOA".equals(trangThai) || "Bị khóa".equals(trangThai)) {
                    hql.append(" AND nd IS NOT NULL AND nd.trangThai IN ('BI_KHOA', N'Bị khóa')");
                } else if ("HOAT_DONG".equals(trangThai) || "Hoạt động".equals(trangThai)) {
                    hql.append(" AND (nd IS NULL OR nd.trangThai IN ('HOAT_DONG', N'Hoạt động'))");
                }
            }
            hql.append(" ORDER BY k.id ASC");

            var q = session.createQuery(hql.toString(), KhachHang.class);
            if (tuKhoa != null && !tuKhoa.isBlank()) {
                q.setParameter("kw", "%" + tuKhoa.trim().toLowerCase() + "%");
            }
            return q.list();
        }
    }

    public KhachHang getOne(Long id) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            List<KhachHang> list = session.createQuery(
                            "SELECT k FROM KhachHang k LEFT JOIN FETCH k.nguoiDung WHERE k.id = :id", KhachHang.class)
                    .setParameter("id", id)
                    .list();
            return list.isEmpty() ? null : list.get(0);
        }
    }

    public KhachHang findByEmail(String email) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery("FROM KhachHang WHERE email = :email", KhachHang.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
    }

    public KhachHang findBySoDienThoai(String soDienThoai) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery("FROM KhachHang WHERE soDienThoai = :soDienThoai", KhachHang.class)
                    .setParameter("soDienThoai", soDienThoai)
                    .uniqueResult();
        }
    }

    public List<DatPhong> layLichSuDatPhong(Long idKhachHang) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT d FROM DatPhong d LEFT JOIN FETCH d.chiTiet WHERE d.khachHang.id = :id ORDER BY d.id DESC",
                            DatPhong.class)
                    .setParameter("id", idKhachHang)
                    .list();
        }
    }

    public TheThanhVien layTheThanhVien(Long idKhachHang) {
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            List<TheThanhVien> rs = session.createQuery(
                            "SELECT t FROM TheThanhVien t JOIN FETCH t.hangThanhVien WHERE t.khachHang.id = :id",
                            TheThanhVien.class)
                    .setParameter("id", idKhachHang)
                    .list();
            return rs.isEmpty() ? null : rs.get(0);
        }
    }

    public void add(KhachHang kh) {
        if (kh.getHoTen() == null || kh.getHoTen().isBlank()) {
            throw new NghiepVuException("Họ tên khách hàng không được để trống.");
        }
        Transaction transaction = null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            session.persist(kh);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Lỗi thêm khách hàng", e);
        }
    }

    public void update(KhachHang kh) {
        if (kh.getHoTen() == null || kh.getHoTen().isBlank()) {
            throw new NghiepVuException("Họ tên khách hàng không được để trống.");
        }
        Transaction transaction = null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            KhachHang khCu = session.find(KhachHang.class, kh.getId());
            if (khCu == null) {
                throw new NghiepVuException("Không tìm thấy khách hàng ID: " + kh.getId());
            }
            khCu.setHoTen(kh.getHoTen());
            khCu.setSoDienThoai(kh.getSoDienThoai());
            khCu.setEmail(kh.getEmail());
            if (khCu.getNguoiDung() != null && kh.getTrangThai() != null && !kh.getTrangThai().isBlank()) {
                khCu.getNguoiDung().setTrangThai(kh.getTrangThai());
                session.merge(khCu.getNguoiDung());
            }
            session.merge(khCu);
            transaction.commit();
        } catch (NghiepVuException e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Lỗi cập nhật khách hàng", e);
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getFACTORY().openSession()) {
            transaction = session.beginTransaction();
            Long countDp = session.createQuery(
                            "SELECT COUNT(d) FROM DatPhong d WHERE d.khachHang.id = :id", Long.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (countDp != null && countDp > 0) {
                throw new NghiepVuException("Không thể xóa khách hàng đã có đơn đặt phòng (" + countDp + " đơn). Hãy khóa tài khoản nếu cần.");
            }
            KhachHang kh = session.find(KhachHang.class, id);
            if (kh != null) {
                session.remove(kh);
            }
            transaction.commit();
        } catch (NghiepVuException e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new NghiepVuException("Không thể xóa khách hàng đang có dữ liệu liên quan.");
        }
    }
}
