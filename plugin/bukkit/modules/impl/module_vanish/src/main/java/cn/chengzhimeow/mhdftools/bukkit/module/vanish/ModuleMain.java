package cn.chengzhimeow.mhdftools.bukkit.module.vanish;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
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
    public boolean isEnable() {
        return ConfigSetting.getInstance().getConfig().enable();
    }

    @Override
    public void onEnable() {
        MHDFToolsAPI.getInstance().getVanishStatusManager().setEnable(this.isEnable());
    }

    @Override
    public void onDisable() {
        MHDFToolsAPI.getInstance().getVanishStatusManager().setEnable(false);
    }

    @Override
    public void reloadConfig() {
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
