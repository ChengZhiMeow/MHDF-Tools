package cn.chengzhimeow.mhdftools.bukkit.module.quitmessage;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.quitmessage.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import org.jetbrains.annotations.NotNull;

public final class ModuleMain extends Module {
    public static ModuleMain instance;

    public ModuleMain() {
        super("quitmessage");
        ModuleMain.instance = this;
    }

    @Override
    public boolean isEnable() {
        return ConfigSetting.getInstance().getConfig().enable();
    }

    @Override
    public void reloadConfig() {
        ConfigSetting.getInstance().saveDefaultFile();
        ConfigSetting.getInstance().update();
        ConfigSetting.getInstance().reload();
    }

    @Override
    public @NotNull AbstractYamlSetting<ConfigSetting.Config> getConfig() {
        return ConfigSetting.getInstance();
    }
}
