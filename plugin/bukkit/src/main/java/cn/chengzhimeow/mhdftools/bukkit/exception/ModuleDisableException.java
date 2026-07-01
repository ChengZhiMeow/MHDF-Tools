package cn.chengzhimeow.mhdftools.bukkit.exception;

import org.jetbrains.annotations.NotNull;

public class ModuleDisableException extends RuntimeException {
    public ModuleDisableException(@NotNull String message) {
        super(message);
    }

    public ModuleDisableException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}
