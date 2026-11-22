package util;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Mở Session -> begin -> chạy công việc -> commit. Lỗi thì rollback.
 * Mọi thao tác DB của Service đều đi qua đây để đảm bảo "1 nghiệp vụ = 1 transaction".
 */
public final class TransactionHelper {
    private TransactionHelper() {}

    public static <T> T chay(Function<Session, T> congViec) {
        try (Session s = HibernateConfig.getFACTORY().openSession()) {
            Transaction tx = s.beginTransaction();
            try {
                T ketQua = congViec.apply(s);
                tx.commit();
                return ketQua;
            } catch (RuntimeException e) {
                // Trigger SQL Server tự ROLLBACK nên tx có thể đã "chết" -> rollback an toàn
                try { if (tx.isActive()) tx.rollback(); } catch (RuntimeException ignored) { }
                throw e;
            }
        }
    }

    public static void chayVoid(Consumer<Session> congViec) {
        chay(s -> { congViec.accept(s); return null; });
    }
}
