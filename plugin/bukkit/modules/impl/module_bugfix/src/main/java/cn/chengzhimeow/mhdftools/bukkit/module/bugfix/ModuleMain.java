package cn.chengzhimeow.mhdftools.bukkit.module.bugfix;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.bugfix.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import org.jetbrains.annotations.NotNull;

public final class ModuleMain extends Module {
    public static ModuleMain instance;

    public ModuleMain() {
        super("bugfix");
        ModuleMain.instance = this;
    }

    @Override
    public boolean isEnable() {
        ConfigSetting.Config config = ConfigSetting.getInstance().getConfig();
        return config.dupe().trident().enable()
                || config.crash().bundle().enable();
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
