package cn.chengzhimeow.mhdftools.bukkit.module.hat;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.config.ModuleConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.config.ModuleLangSetting;
import org.jetbrains.annotations.NotNull;

public final class ModuleMain extends Module {
    public static ModuleMain instance;

    public ModuleMain() {
        super("hat");
        ModuleMain.instance = this;
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public @NotNull ModuleConfigSetting getModuleConfigSetting() {
        return null;
    }

    @Override
    public @NotNull ModuleLangSetting getModuleLangSetting() {
        return null;
    }
}
