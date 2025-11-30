package cn.chengzhimeow.mhdftools.bukkit.module;

import cn.chengzhimeow.mhdftools.bukkit.module.config.ModuleConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.config.ModuleLangSetting;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public abstract class Module {
    private final String id;

    public Module(String id) {
        this.id = id;
    }

    public void onLoad() {}

    public boolean isEnable() {
        return true;
    }

    public void onEnable() {}

    public void onDisable() {}

    public abstract @NotNull ModuleConfigSetting getModuleConfigSetting();

    public abstract @NotNull ModuleLangSetting getModuleLangSetting();
}
