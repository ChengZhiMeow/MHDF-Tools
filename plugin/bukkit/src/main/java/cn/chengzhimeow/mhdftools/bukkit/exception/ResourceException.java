package cn.chengzhimeow.mhdftools.bukkit.exception;

public final class ResourceException extends Exception {
    public ResourceException(String message) {
        super(message);
    }

    public ResourceException(Throwable cause) {
        super(cause);
    }

    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
