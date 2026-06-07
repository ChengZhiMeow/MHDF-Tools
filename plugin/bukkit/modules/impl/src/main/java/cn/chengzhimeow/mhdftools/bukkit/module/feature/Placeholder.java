package cn.chengzhimeow.mhdftools.bukkit.module.feature;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public abstract class Placeholder {
    private final Module module;
    private final boolean enable;
    private final String placeholder;

    public Placeholder(@NotNull Module module, boolean enable, @NotNull String placeholder) {
        this.module = module;
        this.enable = enable;
        this.placeholder = placeholder;
    }

    public Placeholder(@NotNull Module module, @NotNull String placeholder) {
        this(module, true, placeholder);
    }

    public abstract @Nullable String placeholder(@Nullable OfflinePlayer player, @NotNull String[] args);
}
