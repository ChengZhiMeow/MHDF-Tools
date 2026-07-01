package cn.chengzhimeow.mhdftools.bukkit.exception;

import org.jetbrains.annotations.NotNull;

public class ModuleInitException extends RuntimeException {
    public ModuleInitException(@NotNull String message) {
        super(message);
    }

    public ModuleInitException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}
