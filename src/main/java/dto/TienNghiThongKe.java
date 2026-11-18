package dto;

import entity.TienNghi;

/** 1 tiện nghi kèm số liệu kho: tổng trong kho, đã gán cho bao nhiêu phòng, còn lại bao nhiêu. */
public class TienNghiThongKe {
    public static final String CON_HANG = "CON_HANG";
    public static final String HET_KHO  = "HET_KHO";
    public static final String VUOT_KHO = "VUOT_KHO";

    private final TienNghi tienNghi;
    private final long daGan;

    public TienNghiThongKe(TienNghi tienNghi, long daGan) {
        this.tienNghi = tienNghi;
        this.daGan = daGan;
    }

    public TienNghi getTienNghi() { return tienNghi; }
    public long getTong() { return tienNghi.getSoLuong(); }
    public long getDaGan() { return daGan; }
    /** Có thể âm nếu dữ liệu đang gán nhiều phòng hơn số lượng trong kho. */
    public long getConLai() { return getTong() - daGan; }
    public long getThieu() { return Math.max(0, -getConLai()); }
    public String getTinhTrang() {
        long conLai = getConLai();
        return conLai < 0 ? VUOT_KHO : (conLai == 0 ? HET_KHO : CON_HANG);
    }
}
