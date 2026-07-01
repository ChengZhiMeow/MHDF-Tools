package cn.chengzhimeow.mhdftools.bukkit.module.core;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.ModulePriority;
import cn.chengzhimeow.mhdftools.bukkit.module.core.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.core.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import org.jetbrains.annotations.NotNull;

@ModulePriority(2000)
public final class ModuleMain extends Module {
    public static ModuleMain instance;

    public ModuleMain() {
        super("core");
        ModuleMain.instance = this;
    }

    @Override
    public boolean isEnable() {
        return true;
    }

    @Override
    public void reloadConfig() {
        ConfigSetting.getInstance().saveDefaultFile();
        ConfigSetting.getInstance().update();
        ConfigSetting.getInstance().reload();

        LangSetting.getInstance().saveDefaultFile();
        LangSetting.getInstance().update();
        LangSetting.getInstance().reload();
    }

    @Override
    public @NotNull AbstractYamlSetting<ConfigSetting.Config> getConfig() {
        return ConfigSetting.getInstance();
    }
}
