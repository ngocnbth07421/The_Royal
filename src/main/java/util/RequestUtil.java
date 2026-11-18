package util;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/** Đọc & ép kiểu tham số request cho gọn. */
public final class RequestUtil {
    private RequestUtil() {}

    public static String chuoi(HttpServletRequest req, String ten) {
        String v = req.getParameter(ten);
        return (v == null || v.isBlank()) ? null : v.trim();
    }

    public static Long soLong(HttpServletRequest req, String ten) {
        String v = chuoi(req, ten);
        if (v == null) return null;
        try { return Long.valueOf(v); }
        catch (NumberFormatException e) { throw new NghiepVuException("Tham số '" + ten + "' không hợp lệ."); }
    }

    public static int soInt(HttpServletRequest req, String ten, int macDinh) {
        String v = chuoi(req, ten);
        if (v == null) return macDinh;
        try { return Integer.parseInt(v); }
        catch (NumberFormatException e) { throw new NghiepVuException("Tham số '" + ten + "' không hợp lệ."); }
    }

    /** Định dạng yyyy-MM-dd (chuẩn của <input type="date">). */
    public static LocalDate ngay(HttpServletRequest req, String ten) {
        String v = chuoi(req, ten);
        if (v == null) return null;
        try { return LocalDate.parse(v); }
        catch (DateTimeParseException e) { throw new NghiepVuException("Ngày '" + ten + "' không hợp lệ (cần yyyy-MM-dd)."); }
    }

    public static List<Long> danhSachLong(HttpServletRequest req, String ten) {
        List<Long> kq = new ArrayList<>();
        String[] arr = req.getParameterValues(ten);
        if (arr == null) return kq;
        for (String s : arr) {
            if (s == null || s.isBlank()) continue;
            try { kq.add(Long.valueOf(s.trim())); }
            catch (NumberFormatException e) { throw new NghiepVuException("Tham số '" + ten + "' không hợp lệ."); }
        }
        return kq;
    }
}
