package demo;

import dto.PhongYeuCau;
import dto.YeuCauDatPhong;
import entity.DatPhong;
import entity.Phong;
import entity.TienNghi;
import service.DatPhongService;
import service.TienNghiService;
import util.HibernateConfig;

import java.time.LocalDate;
import java.util.List;

/**
 * Chạy thử CHỨC NĂNG (chưa cần giao diện) bằng nút ▶ cạnh hàm main trong IntelliJ.
 * LƯU Ý: ghi thật vào DB (thêm/xóa tiện nghi, tạo + hủy 1 đơn đặt phòng của khách id=1). Xong thì xóa package demo.
 */
public class ChayThuChucNang {
    public static void main(String[] args) {
        TienNghiService tienNghi = new TienNghiService();
        DatPhongService datPhong = new DatPhongService();
        try {
            System.out.println("=== TIỆN NGHI ===");
            tienNghi.danhSach(null).forEach(t -> System.out.println(t.getId() + " | " + t.getTenTienNghi() + " | " + t.getSoLuong()));
            TienNghi moi = tienNghi.them("Máy sấy tóc", 10);
            tienNghi.capNhat(moi.getId(), "Máy sấy tóc Dyson", 12);
            tienNghi.xoa(moi.getId());
            System.out.println("Thêm -> sửa -> xóa tiện nghi: OK");

            System.out.println("=== TRA CỨU PHÒNG (30 ngày tới, 2 người) ===");
            LocalDate nhan = LocalDate.now().plusDays(30), tra = nhan.plusDays(2);
            List<Phong> trong = datPhong.traCuuPhongTrong(nhan, tra, null, null, 2);
            System.out.println("Số phòng trống: " + trong.size());
            trong.stream().limit(5).forEach(p -> System.out.println(p.getMaPhong() + " | " + p.getLoaiPhong().getTen()
                    + " | " + p.getLoaiPhong().getGiaMoiDem()));

            if (!trong.isEmpty()) {
                System.out.println("=== ĐẶT PHÒNG ===");
                DatPhong dp = datPhong.datPhong(new YeuCauDatPhong(1L, null, null, nhan, tra,
                        List.of(new PhongYeuCau(trong.get(0).getId(), 1, 0, 0))));
                System.out.println("Mã đơn " + dp.getMaDatPhong() + " | tiền phòng " + dp.getTienPhong()
                        + " | giảm " + dp.getGiamGia() + " | VAT " + dp.getThueVat()
                        + " | tổng " + dp.getTongTien() + " | cọc " + dp.getTienCoc());

                System.out.println("=== ĐẶT TRÙNG (phải báo lỗi) ===");
                try {
                    datPhong.datPhong(new YeuCauDatPhong(1L, null, null, nhan, tra,
                            List.of(new PhongYeuCau(trong.get(0).getId(), 1, 0, 0))));
                    System.out.println("!!! LỖI: không chặn được đặt trùng");
                } catch (Exception e) {
                    System.out.println("Chặn đúng: " + e.getMessage());
                }

                System.out.println("=== TRA CỨU ĐẶT PHÒNG ===");
                datPhong.traCuuDatPhong(dp.getMaDatPhong(), null, null, null, null)
                        .forEach(d -> System.out.println(d.getMaDatPhong() + " | " + d.getKhachHang().getHoTen() + " | " + d.getTrangThai()));

                System.out.println("=== HỦY PHÒNG ===");
                var ls = datPhong.huyDatPhong(dp.getId(), DatPhong.KHACH_HUY, null, "Chạy thử");
                System.out.println("Hoàn tiền: " + ls.getSoTienHoan());
            }
        } finally {
            HibernateConfig.getFACTORY().close();        }
    }
}
