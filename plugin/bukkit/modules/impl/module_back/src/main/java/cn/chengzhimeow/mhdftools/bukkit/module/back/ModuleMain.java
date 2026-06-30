package cn.chengzhimeow.mhdftools.bukkit.module.back;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.back.config.BackMenuSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.back.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.back.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import org.jetbrains.annotations.NotNull;

public final class ModuleMain extends Module {
    public static ModuleMain instance;

    public ModuleMain() {
        super("back");
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

        BackMenuSetting.getInstance().saveDefaultFile();
        BackMenuSetting.getInstance().update();
        BackMenuSetting.getInstance().reload();
    }

    @Override
    public void onEnable() {
        MHDFToolsAPI.getInstance().getBackDataManager().setEnable(this.isEnable());
    }

    @Override
    public void onDisable() {
        MHDFToolsAPI.getInstance().getBackDataManager().setEnable(false);
    }

    @Override
    public boolean isEnable() {
        return ConfigSetting.getInstance().getConfig().enable();
    }

    @Override
    public @NotNull AbstractYamlSetting<ConfigSetting.Config> getConfig() {
        return ConfigSetting.getInstance();
    }
}
