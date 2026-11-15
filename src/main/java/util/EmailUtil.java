package util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

/**
 * Tiện ích gửi email thật qua giao thức SMTP (Gmail)
 */
public class EmailUtil {

    private static Properties mailProps = new Properties();

    static {
        loadConfig();
    }

    public static synchronized void loadConfig() {
        try (InputStream is = EmailUtil.class.getClassLoader().getResourceAsStream("mail.properties")) {
            if (is != null) {
                mailProps.load(is);
            }
        } catch (Exception e) {
            System.err.println("[EmailUtil] Không thể đọc file mail.properties: " + e.getMessage());
        }
    }

    public static boolean isConfigured() {
        String pwd = mailProps.getProperty("mail.password");
        String user = mailProps.getProperty("mail.username");
        return user != null && !user.trim().isEmpty() && pwd != null && !pwd.trim().isEmpty();
    }

    /**
     * Gửi email OTP bất đồng bộ (chạy nền) để không làm đơ/chậm giao diện web
     */
    public static CompletableFuture<Boolean> guiEmailOtpAsync(String toEmail, String otpCode) {
        return CompletableFuture.supplyAsync(() -> guiEmailOtp(toEmail, otpCode));
    }

    /**
     * Gửi mã OTP xác nhận qua email thật bằng giao thức Gmail SMTP
     */
    public static boolean guiEmailOtp(String toEmail, String otpCode) {
        if (!isConfigured()) {
            System.out.println("[EmailUtil] Cấu hình mail.password chưa được thiết lập. Hãy điền mật khẩu ứng dụng (App Password) vào file mail.properties để gửi email thật.");
            return false;
        }

        try {
            final String username = mailProps.getProperty("mail.username").trim();
            final String password = mailProps.getProperty("mail.password").trim();
            final String fromName = mailProps.getProperty("mail.from.name", "THE ROYAL HOTEL MANAGEMENT");

            Properties props = new Properties();
            props.put("mail.smtp.auth", mailProps.getProperty("mail.smtp.auth", "true"));
            props.put("mail.smtp.starttls.enable", mailProps.getProperty("mail.smtp.starttls.enable", "true"));
            props.put("mail.smtp.host", mailProps.getProperty("mail.smtp.host", "smtp.gmail.com"));
            props.put("mail.smtp.port", mailProps.getProperty("mail.smtp.port", "587"));
            props.put("mail.smtp.ssl.protocols", mailProps.getProperty("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3"));
            props.put("mail.smtp.ssl.trust", mailProps.getProperty("mail.smtp.host", "smtp.gmail.com"));

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, fromName, StandardCharsets.UTF_8.name()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã xác nhận khôi phục mật khẩu - The Royal Hotel");

            String htmlContent = "<div style=\"font-family: Arial, sans-serif; max-width: 580px; margin: 0 auto; padding: 24px; border: 1px solid #e0e0e0; border-radius: 12px; background: #ffffff;\">"
                    + "<div style=\"text-align: center; padding-bottom: 20px; border-bottom: 2px solid #557c56;\">"
                    + "<h2 style=\"color: #557c56; margin: 0; font-size: 24px; letter-spacing: 2px;\">THE ROYAL HOTEL</h2>"
                    + "<p style=\"color: #777; margin: 4px 0 0; font-size: 12px; text-transform: uppercase;\">Hệ Thống Quản Lý Khách Sạn Cao Cấp</p>"
                    + "</div>"
                    + "<div style=\"padding: 24px 0;\">"
                    + "<p style=\"font-size: 15px; color: #333;\">Xin chào,</p>"
                    + "<p style=\"font-size: 14px; color: #555; line-height: 1.6;\">Chúng tôi nhận được yêu cầu lấy lại mật khẩu cho tài khoản liên kết với email: <b>" + toEmail + "</b>.</p>"
                    + "<p style=\"font-size: 14px; color: #555;\">Mã xác thực (OTP) của bạn là:</p>"
                    + "<div style=\"text-align: center; margin: 28px 0;\">"
                    + "<span style=\"display: inline-block; background: #f2f7f3; color: #2e5934; font-size: 32px; font-weight: 800; letter-spacing: 8px; padding: 14px 28px; border-radius: 8px; border: 2px dashed #557c56;\">"
                    + otpCode
                    + "</span>"
                    + "</div>"
                    + "<p style=\"font-size: 13px; color: #888; text-align: center;\">Mã xác nhận có hiệu lực trong vòng <b>10 phút</b>. Vui lòng không chia sẻ mã này cho bất kỳ ai.</p>"
                    + "<p style=\"font-size: 13px; color: #888;\">Nếu bạn không yêu cầu đặt lại mật khẩu, xin hãy bỏ qua email này.</p>"
                    + "</div>"
                    + "<div style=\"border-top: 1px solid #eee; padding-top: 16px; text-align: center; color: #999; font-size: 12px;\">"
                    + "<p style=\"margin: 2px 0;\">The Royal Hotel Management System</p>"
                    + "<p style=\"margin: 2px 0;\">Hotline: 1900 8888 | Email hỗ trợ: support@theroyal.com</p>"
                    + "</div>"
                    + "</div>";

            message.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("[EmailUtil] Đã gửi thành công mã OTP (" + otpCode + ") đến hòm thư Gmail thật: " + toEmail);
            return true;
        } catch (Exception e) {
            System.err.println("[EmailUtil] Gửi email thất bại tới " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
