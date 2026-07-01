package cn.chengzhimeow.mhdftools.bukkit.module.custommenu;

import cn.chengzhimeow.mhdftools.bukkit.common.action.ActionManager;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.action.OpenCustomMenuAction;
import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.config.MenuSetting;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import org.jetbrains.annotations.NotNull;

public final class ModuleMain extends Module {
    public static ModuleMain instance;

    public ModuleMain() {
        super("custommenu");
        ModuleMain.instance = this;
    }

    @Override
    public void onLoad() {
        ActionManager.getInstance().register("menu", OpenCustomMenuAction.class);
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

        LangSetting.getInstance().saveDefaultFile();
        LangSetting.getInstance().update();
        LangSetting.getInstance().reload();

        MenuSetting.getInstance().saveDefaultFile();
        MenuSetting.getInstance().update();
        MenuSetting.getInstance().reload();
    }

    @Override
    public @NotNull AbstractYamlSetting<ConfigSetting.Config> getConfig() {
        return ConfigSetting.getInstance();
    }
}
