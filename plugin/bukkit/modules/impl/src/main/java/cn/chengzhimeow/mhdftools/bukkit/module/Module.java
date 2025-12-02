package cn.chengzhimeow.mhdftools.bukkit.module;

import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class Module {
    private final String id;
    private JavaPlugin plugin;

    public Module(String id) {
        this.id = id;
    }

    public void onLoad() {
    }

    public boolean isEnable() {
        return true;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public abstract @NotNull AbstractYamlSetting getConfig();
}
