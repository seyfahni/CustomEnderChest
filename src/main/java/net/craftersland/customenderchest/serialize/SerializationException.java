package net.craftersland.customenderchest.serialize;

public class SerializationException extends Exception {

    private static final long serialVersionUID = -3843815859658952126L;

    public SerializationException() {
    }

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }
}
