package cn.chengzhimeow.mhdftools.bukkit.module.feature;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.config.ConfigUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Listener implements org.bukkit.event.Listener {
    private final Module module;
    private final boolean enable;

    public Listener(@NotNull Module module, @NotNull List<String> enableKeyList) {
        this.module = module;
        this.enable = ConfigUtil.equalsTrue(module.getConfig().getData(), enableKeyList);
    }

    public Listener( @NotNull Module module) {
        this(module, new ArrayList<>());
    }
}
