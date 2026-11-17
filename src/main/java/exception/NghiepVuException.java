package exception;

/** Lỗi nghiệp vụ (người dùng nhập sai, phòng đã bị đặt, ...). Message hiển thị thẳng ra giao diện. */
public class NghiepVuException extends RuntimeException {
    public NghiepVuException(String message) {
        super(message);
    }
}
