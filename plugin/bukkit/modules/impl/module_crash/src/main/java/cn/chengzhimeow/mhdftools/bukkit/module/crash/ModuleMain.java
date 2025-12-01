package cn.chengzhimeow.mhdftools.bukkit.module.crash;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.crash.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.crash.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import org.jetbrains.annotations.NotNull;

public final class ModuleMain extends Module {
    public static ModuleMain instance;

    public ModuleMain() {
        super("crash");
        ModuleMain.instance = this;
    }

    @Override
    public void onLoad() {
        ConfigSetting.getInstance().saveDefaultFile();
        ConfigSetting.getInstance().update();
        ConfigSetting.getInstance().reload();

        LangSetting.getInstance().saveDefaultFile();
        LangSetting.getInstance().update();
        LangSetting.getInstance().reload();
    }

    @Override
    public @NotNull AbstractYamlSetting getConfig() {
        return ConfigSetting.getInstance();
    }
}
