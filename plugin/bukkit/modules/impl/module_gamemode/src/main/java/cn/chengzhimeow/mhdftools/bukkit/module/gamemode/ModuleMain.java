package cn.chengzhimeow.mhdftools.bukkit.module.gamemode;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.gamemode.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.gamemode.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.gamemode.message.GameModeMessage;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import org.jetbrains.annotations.NotNull;

public final class ModuleMain extends Module {
    public static ModuleMain instance;

    public ModuleMain() {
        super("gamemode");
        ModuleMain.instance = this;
    }

    @Override
    public boolean isEnable() {
        return ConfigSetting.getInstance().getConfig().enable();
    }

    @Override
    public void onEnable() {
        MHDFToolsBukkit.getInstance().getRedisManager().register(GameModeMessage.ID, GameModeMessage.CODEC);
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
