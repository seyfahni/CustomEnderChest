package net.craftersland.customenderchest.transform;

public class DataTransformationException extends Exception {

    private static final long serialVersionUID = 8904416848594276358L;

    public DataTransformationException() {
    }

    public DataTransformationException(String message) {
        super(message);
    }

    public DataTransformationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataTransformationException(Throwable cause) {
        super(cause);
    }
}
