package repository;

import controller.NghiepVuException;
import entity.DatPhong;
import entity.ThanhToan;
import entity.TheThanhVien;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateConfig;

import java.math.BigDecimal;
import java.util.List;

public class ThanhToanRepository {
    private Session getSession() {
        return HibernateConfig.getFACTORY().openSession();
    }

    public List<ThanhToan> findAll() {
        return search(null, null, null);
    }

    public List<ThanhToan> search(String tuKhoa, String phuongThuc, String trangThai) {
        try (Session session = getSession()) {
            StringBuilder hql = new StringBuilder(
                    "SELECT tt FROM ThanhToan tt JOIN FETCH tt.datPhong dp LEFT JOIN FETCH dp.khachHang kh LEFT JOIN FETCH tt.theThanhVien WHERE 1 = 1");
            if (tuKhoa != null && !tuKhoa.isBlank()) {
                hql.append(" AND (LOWER(dp.maDatPhong) LIKE :kw OR LOWER(kh.hoTen) LIKE :kw)");
            }
            if (phuongThuc != null && !phuongThuc.isBlank()) {
                hql.append(" AND tt.phuongThucThanhToan = :pt");
            }
            if (trangThai != null && !trangThai.isBlank()) {
                hql.append(" AND tt.trangThai = :tt");
            }
            hql.append(" ORDER BY tt.id DESC");
            var q = session.createQuery(hql.toString(), ThanhToan.class);
            if (tuKhoa != null && !tuKhoa.isBlank()) {
                q.setParameter("kw", "%" + tuKhoa.trim().toLowerCase() + "%");
            }
            if (phuongThuc != null && !phuongThuc.isBlank()) {
                q.setParameter("pt", phuongThuc);
            }
            if (trangThai != null && !trangThai.isBlank()) {
                q.setParameter("tt", trangThai);
            }
            return q.list();
        }
    }

    public ThanhToan findById(Long id) {
        try (Session session = getSession()) {
            List<ThanhToan> rs = session.createQuery(
                            "SELECT tt FROM ThanhToan tt JOIN FETCH tt.datPhong dp LEFT JOIN FETCH dp.khachHang LEFT JOIN FETCH tt.theThanhVien WHERE tt.id = :id",
                            ThanhToan.class)
                    .setParameter("id", id)
                    .list();
            return rs.isEmpty() ? null : rs.get(0);
        }
    }

    public List<DatPhong> findAllDatPhong() {
        try (Session session = getSession()) {
            return session.createQuery(
                    "SELECT DISTINCT d FROM DatPhong d LEFT JOIN FETCH d.khachHang LEFT JOIN FETCH d.chiTiet ORDER BY d.id DESC",
                    DatPhong.class).list();
        }
    }

    public List<TheThanhVien> findAllTheThanhVienHoatDong() {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM TheThanhVien WHERE trangThai IN (N'Hoạt động', 'HOAT_DONG')", TheThanhVien.class)
                    .list();
        }
    }

    private void dongBoTienThanhToanDatPhong(Session session, Long idDatPhong) {
        BigDecimal tongDaThu = session.createQuery(
                        "SELECT COALESCE(SUM(tt.tienThanhToan), 0) FROM ThanhToan tt WHERE tt.datPhong.id = :id AND tt.trangThai IN (N'Thành công', 'THANH_CONG')",
                        BigDecimal.class)
                .setParameter("id", idDatPhong)
                .uniqueResult();
        DatPhong dp = session.find(DatPhong.class, idDatPhong);
        if (dp != null && tongDaThu != null) {
            dp.setTienThanhToan(tongDaThu);
            session.merge(dp);
        }
    }

    public void capNhatTrangThai(Long id, String trangThaiMoi) {
        if (trangThaiMoi == null || trangThaiMoi.isBlank()) {
            throw new NghiepVuException("Vui lòng chọn trạng thái mới.");
        }
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            ThanhToan tt = session.find(ThanhToan.class, id);
            if (tt == null) {
                throw new NghiepVuException("Không tìm thấy giao dịch thanh toán.");
            }
            tt.setTrangThai(trangThaiMoi);
            session.merge(tt);
            session.flush();
            dongBoTienThanhToanDatPhong(session, tt.getDatPhong().getId());
            transaction.commit();
        } catch (NghiepVuException e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Lỗi cập nhật trạng thái thanh toán", e);
        }
    }

    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            ThanhToan tt = session.find(ThanhToan.class, id);
            if (tt != null) {
                Long idDatPhong = tt.getDatPhong().getId();
                session.remove(tt);
                session.flush();
                dongBoTienThanhToanDatPhong(session, idDatPhong);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Lỗi xóa thanh toán", e);
        }
    }

    public List<ThanhToan> layDanhSach() { return findAll(); }
    public List<ThanhToan> layDanhSach(String tuKhoa, String phuongThuc, String trangThai) { return search(tuKhoa, phuongThuc, trangThai); }
    public List<DatPhong> layDanhSachDatPhong() { return findAllDatPhong(); }
    public List<TheThanhVien> layDanhSachTheThanhVien() { return findAllTheThanhVienHoatDong(); }

    public void taoGiaoDichThanhToan(Long idDatPhong, Long idTheThanhVien, String phuongThuc, BigDecimal soTien, String trangThai) {
        if (idDatPhong == null) {
            throw new NghiepVuException("Vui lòng chọn đơn đặt phòng cần thanh toán.");
        }
        if (soTien == null || soTien.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NghiepVuException("Số tiền thanh toán phải lớn hơn 0.");
        }
        boolean laThe = "Thẻ".equals(phuongThuc) || "THE".equals(phuongThuc);
        if (laThe && idTheThanhVien == null) {
            throw new NghiepVuException("Khi chọn phương thức Thẻ thành viên, bắt buộc phải chọn thẻ của khách!");
        }

        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            DatPhong dp = session.find(DatPhong.class, idDatPhong);
            if (dp == null) throw new NghiepVuException("Không tìm thấy đơn đặt phòng.");

            ThanhToan tt = new ThanhToan();
            tt.setDatPhong(dp);
            if (laThe && idTheThanhVien != null) {
                TheThanhVien ttv = session.find(TheThanhVien.class, idTheThanhVien);
                if (ttv == null) throw new NghiepVuException("Không tìm thấy thẻ thành viên.");
                tt.setTheThanhVien(ttv);
            } else {
                tt.setTheThanhVien(null);
            }
            tt.setPhuongThucThanhToan(phuongThuc);
            tt.setTienThanhToan(soTien);
            tt.setTrangThai(trangThai != null ? trangThai : "Thành công");

            session.persist(tt);
            session.flush();
            dongBoTienThanhToanDatPhong(session, idDatPhong);
            transaction.commit();
        } catch (NghiepVuException e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new RuntimeException("Lỗi tạo giao dịch thanh toán", e);
        }
    }

    public void doiTrangThai(Long idThanhToan, String trangThaiMoi) {
        capNhatTrangThai(idThanhToan, trangThaiMoi);
    }
}
