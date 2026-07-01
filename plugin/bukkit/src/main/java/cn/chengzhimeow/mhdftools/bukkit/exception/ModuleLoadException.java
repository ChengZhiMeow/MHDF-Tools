package cn.chengzhimeow.mhdftools.bukkit.exception;

import org.jetbrains.annotations.NotNull;

public class ModuleLoadException extends RuntimeException {
    public ModuleLoadException(@NotNull String message) {
        super(message);
    }

    public ModuleLoadException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}
