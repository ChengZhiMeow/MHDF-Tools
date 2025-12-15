package cn.chengzhimeow.mhdftools.bukkit.module.timeaction;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.timeaction.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import org.jetbrains.annotations.NotNull;

public final class ModuleMain extends Module {
    public static ModuleMain instance;

    public ModuleMain() {
        super("timeaction");
        ModuleMain.instance = this;
    }

    @Override
    public void onLoad() {
        ConfigSetting.getInstance().saveDefaultFile();
        ConfigSetting.getInstance().update();
        ConfigSetting.getInstance().reload();
    }

    @Override
    public @NotNull AbstractYamlSetting getConfig() {
        return ConfigSetting.getInstance();
    }
}
