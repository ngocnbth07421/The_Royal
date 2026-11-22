package util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/** Thông báo "hiện 1 lần" sau khi redirect (Post-Redirect-Get). */
public final class FlashMessage {
    private FlashMessage() {}

    public static void thanhCong(HttpServletRequest req, String msg) { req.getSession().setAttribute("flash_thongBao", msg); }
    public static void loi(HttpServletRequest req, String msg)       { req.getSession().setAttribute("flash_loi", msg); }

    /** Chuyển flash từ session sang request rồi xóa khỏi session. Gọi đầu mỗi doGet. */
    public static void nap(HttpServletRequest req) {
        HttpSession ss = req.getSession(false);
        if (ss == null) return;
        for (String k : new String[]{"thongBao", "loi"}) {
            Object v = ss.getAttribute("flash_" + k);
            if (v != null) { req.setAttribute(k, v); ss.removeAttribute("flash_" + k); }
        }
    }
}
