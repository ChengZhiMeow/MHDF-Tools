package cn.chengzhimeow.mhdftools.bukkit.module.invsee;

import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.invsee.config.ArmorMenuSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.invsee.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.invsee.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import org.jetbrains.annotations.NotNull;

public final class ModuleMain extends Module {
    public static ModuleMain instance;

    public ModuleMain() {
        super("invsee");
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

        ArmorMenuSetting.getInstance().saveDefaultFile();
        ArmorMenuSetting.getInstance().update();
        ArmorMenuSetting.getInstance().reload();
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
