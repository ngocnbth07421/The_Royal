package service;

import entity.MaGiamGia;
import exception.NghiepVuException;
import repository.MaGiamGiaRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Tầng xử lý logic nghiệp vụ cho Mã Giảm Giá.
 */
public class MaGiamGiaService {
    private final MaGiamGiaRepository repo = new MaGiamGiaRepository();

    public List<MaGiamGia> layDanhSach() { return repo.findAll(); }
    public MaGiamGia timTheoId(Long id) { return repo.findById(id); }

    /**
     * Nghiệp vụ: Kiểm tra điều kiện hợp lệ của Voucher trước khi lưu:
     * 1. Chuẩn hóa mã code viết hoa, không trùng lặp (UNIQUE).
     * 2. Nếu giảm theo PHAN_TRAM: Giá trị bắt buộc từ 1% đến tối đa 50%.
     * 3. Nếu giảm theo SO_TIEN: Giá trị phải lớn hơn 0.
     * 4. Ngày kết thúc phải sau ngày bắt đầu.
     */
    public void luuMaGiamGia(MaGiamGia mgg) {
        if (mgg.getMaGiamGia() == null || mgg.getMaGiamGia().trim().isEmpty()) {
            throw new NghiepVuException("Mã giảm giá không được để trống.");
        }
        String code = mgg.getMaGiamGia().trim().toUpperCase();
        MaGiamGia trung = repo.findByMaCode(code);
        if (trung != null && (mgg.getId() == null || !trung.getId().equals(mgg.getId()))) {
            throw new NghiepVuException("Mã giảm giá '" + code + "' đã tồn tại trên hệ thống.");
        }

        if (mgg.getGiaTri() == null || mgg.getGiaTri().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NghiepVuException("Giá trị giảm phải lớn hơn 0.");
        }

        // Ràng buộc nghiệp vụ: Giảm theo phần trăm tối thiểu 1% và tối đa 50%
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
        repo.saveOrUpdate(mgg);
    }

    public void xoaMaGiamGia(Long id) {
        repo.delete(id);
    }
}