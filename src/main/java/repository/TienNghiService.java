package repository;

import entity.TienNghiChiTiet;
import entity.TienNghiThongKe;
import entity.Phong;
import entity.TienNghi;
import util.NghiepVuException;
import jakarta.persistence.LockModeType;
import util.TransactionHelper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Nghiệp vụ "Quản lý tiện nghi": Thêm / Sửa / Xóa / Xem + gán tiện nghi cho phòng.
 *
 * Quy tắc kho (kiểm tra 2 chiều):
 *   - tien_nghi.so_luong = TỔNG số lượng trong kho.
 *   - Mỗi phòng có tiện nghi đó = đã dùng 1 đơn vị  ->  còn lại = tổng - số phòng đang có.
 *   - Gán thêm cho phòng: không được vượt số còn lại trong kho.
 *   - Sửa số lượng kho: không được nhỏ hơn số phòng đang có.
 */
public class TienNghiService {
    private static final int GIOI_HAN_DANH_SACH_PHONG = 200;

    private final TienNghiRepository repo = new TienNghiRepository();

    // ---------------------------------------------------------------- XEM
    public List<TienNghi> danhSach(String tuKhoa) {
        return TransactionHelper.chay(s -> repo.timTatCa(s, tuKhoa));
    }

    /** Danh sách tiện nghi kèm số liệu kho. Tìm theo mã (TN001) hoặc tên. */
    public List<TienNghiThongKe> danhSachThongKe(String tuKhoa) {
        return TransactionHelper.chay(s -> {
            Map<Long, Long> dem = repo.demPhongTheoTienNghi(s);
            String kw = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
            List<TienNghiThongKe> kq = new ArrayList<>();
            for (TienNghi t : repo.timTatCa(s, null)) {
                if (!kw.isEmpty() && !t.getTenTienNghi().toLowerCase().contains(kw)
                        && !t.getMaTienNghi().toLowerCase().contains(kw)) continue;
                kq.add(new TienNghiThongKe(t, dem.getOrDefault(t.getId(), 0L)));
            }
            kq.sort(Comparator.comparing(x -> x.getTienNghi().getId()));
            return kq;
        });
    }

    public TienNghi layTheoId(Long id) {
        TienNghi t = TransactionHelper.chay(s -> repo.timTheoId(s, id));
        if (t == null) throw new NghiepVuException("Không tìm thấy tiện nghi.");
        return t;
    }

    /** Trang chi tiết 1 tiện nghi: các phòng ĐÃ có và CHƯA có. */
    public TienNghiChiTiet layChiTiet(Long id, String tuKhoaPhong) {
        return TransactionHelper.chay(s -> {
            TienNghi t = repo.timTheoId(s, id);
            if (t == null) throw new NghiepVuException("Không tìm thấy tiện nghi.");
            List<Phong> co = repo.timPhongCoTienNghi(s, id);
            long tongChua = repo.demTongPhong(s) - co.size();
            List<Phong> chua = repo.timPhongChuaCoTienNghi(s, id, tuKhoaPhong, GIOI_HAN_DANH_SACH_PHONG);
            return new TienNghiChiTiet(new TienNghiThongKe(t, co.size()), co, chua, tongChua, tuKhoaPhong);
        });
    }

    // ---------------------------------------------------------------- THÊM
    public TienNghi them(String ten, Integer soLuong) {
        String tenSach = kiemTraDauVao(ten, soLuong);
        return TransactionHelper.chay(s -> {
            if (repo.tonTaiTen(s, tenSach, null))
                throw new NghiepVuException("Tiện nghi '" + tenSach + "' đã tồn tại.");
            TienNghi t = new TienNghi(tenSach, soLuong);
            repo.them(s, t);
            return t;
        });
    }

    // ---------------------------------------------------------------- SỬA
    public TienNghi capNhat(Long id, String ten, Integer soLuong) {
        String tenSach = kiemTraDauVao(ten, soLuong);
        return TransactionHelper.chay(s -> {
            TienNghi t = repo.timTheoId(s, id);
            if (t == null) throw new NghiepVuException("Không tìm thấy tiện nghi.");
            if (repo.tonTaiTen(s, tenSach, id))
                throw new NghiepVuException("Tiện nghi '" + tenSach + "' đã tồn tại.");
            long daGan = repo.demPhongDangDung(s, id);
            if (soLuong < daGan)
                throw new NghiepVuException("Số lượng trong kho (" + soLuong + ") không được nhỏ hơn số phòng đang có tiện nghi này ("
                        + daGan + "). Hãy gỡ bớt khỏi phòng hoặc nhập số lượng lớn hơn.");
            t.setTenTienNghi(tenSach);   // entity đang "managed" -> commit tự UPDATE
            t.setSoLuong(soLuong);
            return t;
        });
    }

    // ---------------------------------------------------------------- XÓA
    /** Không cho xóa nếu còn phòng đang dùng (tránh vi phạm FK tien_nghi_phong). */
    public void xoa(Long id) {
        TransactionHelper.chayVoid(s -> {
            TienNghi t = repo.timTheoId(s, id);
            if (t == null) throw new NghiepVuException("Tiện nghi không tồn tại (có thể đã bị xóa).");
            long soPhong = repo.demPhongDangDung(s, id);
            if (soPhong > 0)
                throw new NghiepVuException("Không thể xóa: tiện nghi '" + t.getTenTienNghi()
                        + "' đang được gắn cho " + soPhong + " phòng. Hãy gỡ khỏi các phòng trước.");
            repo.xoa(s, t);
        });
    }

    // ---------------------------------------------------------------- CHIỀU PHÒNG -> TIỆN NGHI
    public Phong layPhong(Long idPhong) {
        Phong p = TransactionHelper.chay(s -> s.find(Phong.class, idPhong));
        if (p == null) throw new NghiepVuException("Không tìm thấy phòng.");
        return p;
    }

    public List<TienNghi> tienNghiCuaPhong(Long idPhong) {
        return TransactionHelper.chay(s -> {
            Phong p = s.find(Phong.class, idPhong);
            if (p == null) throw new NghiepVuException("Không tìm thấy phòng.");
            return p.getTienNghis().stream()
                    .sorted(Comparator.comparing(TienNghi::getId))
                    .collect(Collectors.toList());   // đọc trong session vì collection là LAZY
        });
    }

    /** Đặt lại toàn bộ tiện nghi của 1 phòng = đúng danh sách idTienNghi. Có kiểm tra kho cho tiện nghi mới thêm. */
    public void ganChoPhong(Long idPhong, Collection<Long> idTienNghi) {
        Set<Long> muonCo = new HashSet<>(idTienNghi);
        TransactionHelper.chayVoid(s -> {
            Phong p = s.find(Phong.class, idPhong);
            if (p == null) throw new NghiepVuException("Không tìm thấy phòng.");
            List<TienNghi> moi = repo.timTheoDanhSachId(s, new ArrayList<>(muonCo));
            if (moi.size() != muonCo.size())
                throw new NghiepVuException("Có tiện nghi không tồn tại trong danh sách đã chọn.");

            p.getTienNghis().removeIf(t -> !muonCo.contains(t.getId()));          // gỡ cái không chọn nữa -> trả về kho
            Set<Long> daCo = p.getTienNghis().stream().map(TienNghi::getId).collect(Collectors.toSet());

            List<TienNghi> canThem = moi.stream().filter(t -> !daCo.contains(t.getId()))
                    .sorted(Comparator.comparing(TienNghi::getId)).collect(Collectors.toList());
            for (TienNghi t : canThem) {                                           // kiểm tra kho trước khi thêm
                s.lock(t, LockModeType.PESSIMISTIC_WRITE);
                long dung = repo.demPhongDangDung(s, t.getId());
                if (dung >= t.getSoLuong())
                    throw new NghiepVuException("Tiện nghi '" + t.getTenTienNghi() + "' đã hết trong kho (tổng "
                            + t.getSoLuong() + ", đã gắn cho " + dung + " phòng).");
                p.getTienNghis().add(t);
            }
        });
    }

    // ---------------------------------------------------------------- CHIỀU TIỆN NGHI -> PHÒNG
    /**
     * Gán (gan = true) hoặc gỡ (gan = false) 1 tiện nghi cho nhiều phòng cùng lúc.
     * @return số phòng thực sự thay đổi
     */
    public int capNhatPhongChoTienNghi(Long idTienNghi, List<Long> idPhongs, boolean gan) {
        if (idTienNghi == null) throw new NghiepVuException("Thiếu mã tiện nghi.");
        if (idPhongs == null || idPhongs.isEmpty()) throw new NghiepVuException("Vui lòng chọn ít nhất 1 phòng.");
        Set<Long> ids = new HashSet<>(idPhongs);

        return TransactionHelper.chay(s -> {
            TienNghi t = repo.timTheoId(s, idTienNghi);
            if (t == null) throw new NghiepVuException("Không tìm thấy tiện nghi.");
            s.lock(t, LockModeType.PESSIMISTIC_WRITE);   // khóa để 2 người không cùng lấy hết kho

            List<Phong> phongs = repo.timPhongTheoDanhSachId(s, new ArrayList<>(ids));
            if (phongs.size() != ids.size()) throw new NghiepVuException("Có phòng không tồn tại trong danh sách đã chọn.");

            int doi = 0;
            if (gan) {
                List<Phong> canThem = phongs.stream()
                        .filter(p -> p.getTienNghis().stream().noneMatch(x -> x.getId().equals(idTienNghi)))
                        .collect(Collectors.toList());
                long daGan = repo.demPhongDangDung(s, idTienNghi);
                long conLai = t.getSoLuong() - daGan;
                if (canThem.size() > conLai)
                    throw new NghiepVuException("Kho của '" + t.getTenTienNghi() + "' chỉ còn " + Math.max(0, conLai)
                            + " (tổng " + t.getSoLuong() + ", đã gắn cho " + daGan + " phòng) nhưng bạn chọn "
                            + canThem.size() + " phòng.");
                for (Phong p : canThem) p.getTienNghis().add(t);
                doi = canThem.size();
            } else {
                for (Phong p : phongs) if (p.getTienNghis().removeIf(x -> x.getId().equals(idTienNghi))) doi++;
            }
            return doi;
        });
    }

    // ---------------------------------------------------------------- VALIDATE
    private String kiemTraDauVao(String ten, Integer soLuong) {
        if (ten == null || ten.isBlank()) throw new NghiepVuException("Tên tiện nghi không được để trống.");
        String sach = ten.trim().replaceAll("\\s+", " ");
        if (sach.length() > 100) throw new NghiepVuException("Tên tiện nghi tối đa 100 ký tự.");
        if (soLuong == null || soLuong < 0) throw new NghiepVuException("Số lượng phải là số nguyên >= 0.");
        return sach;
    }
}
