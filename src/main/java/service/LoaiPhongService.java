package service;

import entity.HangPhong;
import entity.LoaiGiuong;
import entity.LoaiPhong;
import exception.NghiepVuException;
import repository.LoaiPhongRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Tầng xử lý logic nghiệp vụ (Business Logic) cho Loại Phòng.
 * Tận dụng class NghiepVuException có sẵn trong dự án để ném thông báo lỗi thân thiện.
 */
public class LoaiPhongService {
    private final LoaiPhongRepository repo = new LoaiPhongRepository();

    public List<LoaiPhong> layDanhSach() { return repo.findAll(); }
    public LoaiPhong timTheoId(Long id) { return repo.findById(id); }
    public List<HangPhong> layDanhSachHangPhong() { return repo.findAllHangPhong(); }
    public List<LoaiGiuong> layDanhSachLoaiGiuong() { return repo.findAllLoaiGiuong(); }

    /**
     * Nghiệp vụ: Kiểm tra tính hợp lệ của dữ liệu loại phòng trước khi lưu vào CSDL:
     * 1. Tên loại phòng không được rỗng.
     * 2. Giá mỗi đêm phải > 0 (khớp CHECK (gia_moi_dem > 0) trong SQL).
     * 3. Cặp (Hạng phòng + Loại giường) không được trùng lặp (khớp UNIQUE (id_hang_phong, id_loai_giuong)).
     */
    public void luuLoaiPhong(Long id, String ten, BigDecimal giaMoiDem, String moTa, Long idHangPhong, Long idLoaiGiuong) {
        if (ten == null || ten.trim().isEmpty()) {
            throw new NghiepVuException("Tên loại phòng không được để trống.");
        }
        if (giaMoiDem == null || giaMoiDem.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NghiepVuException("Giá mỗi đêm bắt buộc phải lớn hơn 0.");
        }

        LoaiPhong trung = repo.findByHangVaGiuong(idHangPhong, idLoaiGiuong);
        if (trung != null && (id == null || !trung.getId().equals(id))) {
            throw new NghiepVuException("Tổ hợp Hạng phòng và Loại giường này đã tồn tại ở loại phòng: " + trung.getTen());
        }

        LoaiPhong lp = (id != null) ? repo.findById(id) : new LoaiPhong();
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

        repo.saveOrUpdate(lp);
    }

    /**
     * Nghiệp vụ: Xóa loại phòng theo ID.
     */
    public void xoaLoaiPhong(Long id) {
        repo.delete(id);
    }
}
