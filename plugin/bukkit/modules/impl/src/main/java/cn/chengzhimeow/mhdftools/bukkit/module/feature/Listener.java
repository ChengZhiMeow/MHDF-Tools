package cn.chengzhimeow.mhdftools.bukkit.module.feature;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class Listener implements org.bukkit.event.Listener {
    private final Module module;
    private final boolean enable;

    public Listener(@NotNull Module module, boolean enable) {
        this.module = module;
        this.enable = enable;
    }

    public Listener(@NotNull Module module) {
        this(module, true);
    }
}
