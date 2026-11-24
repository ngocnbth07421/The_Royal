package controller;

public class NghiepVuException extends RuntimeException {
    public NghiepVuException(String message) {
        super(message);
    }

    public NghiepVuException(String message, Throwable cause) {
        super(message, cause);
    }
}
