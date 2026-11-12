package service;

import entity.Phong;
import entity.TienNghi;
import exception.NghiepVuException;
import repository.TienNghiRepository;
import util.TransactionHelper;

import java.util.*;
import java.util.stream.Collectors;

/** Nghiệp vụ "Quản lý tiện nghi": Thêm / Sửa / Xóa / Xem + gán tiện nghi cho phòng. */
public class TienNghiService {
    private final TienNghiRepository repo = new TienNghiRepository();

    // ---------------------------------------------------------------- XEM
    public List<TienNghi> danhSach(String tuKhoa) {
        return TransactionHelper.chay(s -> repo.timTatCa(s, tuKhoa));
    }

    public TienNghi layTheoId(Long id) {
        TienNghi t = TransactionHelper.chay(s -> repo.timTheoId(s, id));
        if (t == null) throw new NghiepVuException("Không tìm thấy tiện nghi.");
        return t;
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

    // ---------------------------------------------------------------- GÁN TIỆN NGHI CHO PHÒNG
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
                    .sorted(Comparator.comparing(TienNghi::getTenTienNghi))
                    .collect(Collectors.toList());   // đọc trong session vì collection là LAZY
        });
    }

    /** Đặt lại toàn bộ tiện nghi của 1 phòng = đúng danh sách idTienNghi truyền vào. */
    public void ganChoPhong(Long idPhong, Collection<Long> idTienNghi) {
        Set<Long> muonCo = new HashSet<>(idTienNghi);
        TransactionHelper.chayVoid(s -> {
            Phong p = s.find(Phong.class, idPhong);
            if (p == null) throw new NghiepVuException("Không tìm thấy phòng.");
            List<TienNghi> moi = repo.timTheoDanhSachId(s, new ArrayList<>(muonCo));
            if (moi.size() != muonCo.size())
                throw new NghiepVuException("Có tiện nghi không tồn tại trong danh sách đã chọn.");

            p.getTienNghis().removeIf(t -> !muonCo.contains(t.getId()));          // gỡ cái không chọn nữa
            Set<Long> daCo = p.getTienNghis().stream().map(TienNghi::getId).collect(Collectors.toSet());
            for (TienNghi t : moi) if (!daCo.contains(t.getId())) p.getTienNghis().add(t);  // thêm cái mới chọn
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
