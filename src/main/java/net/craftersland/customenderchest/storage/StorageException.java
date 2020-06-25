package net.craftersland.customenderchest.storage;

public class StorageException extends Exception {

    private static final long serialVersionUID = 4209955716302003697L;

    public StorageException() {
    }

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(Throwable cause) {
        super(cause);
    }
}
