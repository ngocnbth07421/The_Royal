package dto;

import entity.Phong;

import java.util.List;

/** Dữ liệu trang chi tiết 1 tiện nghi: phòng ĐÃ có và phòng CHƯA có tiện nghi này. */
public class TienNghiChiTiet {
    private final TienNghiThongKe thongKe;
    private final List<Phong> phongCo;
    private final List<Phong> phongChua;
    private final long tongPhongChua;
    private final String tuKhoa;

    public TienNghiChiTiet(TienNghiThongKe thongKe, List<Phong> phongCo, List<Phong> phongChua,
                           long tongPhongChua, String tuKhoa) {
        this.thongKe = thongKe;
        this.phongCo = phongCo;
        this.phongChua = phongChua;
        this.tongPhongChua = tongPhongChua;
        this.tuKhoa = tuKhoa;
    }

    public TienNghiThongKe getThongKe() { return thongKe; }
    public List<Phong> getPhongCo() { return phongCo; }
    public List<Phong> getPhongChua() { return phongChua; }
    /** Tổng số phòng chưa có (danh sách hiển thị có thể bị cắt bớt). */
    public long getTongPhongChua() { return tongPhongChua; }
    public String getTuKhoa() { return tuKhoa; }
    public boolean isBiCat() { return tongPhongChua > phongChua.size() && (tuKhoa == null || tuKhoa.isBlank()); }
}
