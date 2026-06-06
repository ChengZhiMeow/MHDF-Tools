package cn.chengzhimeow.mhdftools.bukkit.module.vanish;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.config.BossBarSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import org.jetbrains.annotations.NotNull;

public final class ModuleMain extends Module {
    public static ModuleMain instance;

    public ModuleMain() {
        super("vanish");
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

        BossBarSetting.getInstance().reload();
    }

    @Override
    public @NotNull AbstractYamlSetting<ConfigSetting.Config> getConfig() {
        return ConfigSetting.getInstance();
    }
}
