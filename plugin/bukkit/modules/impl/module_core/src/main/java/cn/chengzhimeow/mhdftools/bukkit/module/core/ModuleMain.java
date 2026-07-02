package cn.chengzhimeow.mhdftools.bukkit.module.core;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.ModulePriority;
import cn.chengzhimeow.mhdftools.bukkit.module.core.cache.ServerTeleportCache;
import cn.chengzhimeow.mhdftools.bukkit.module.core.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.core.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.core.listener.ServerTeleport;
import cn.chengzhimeow.mhdftools.bukkit.module.core.message.PlayerMessage;
import cn.chengzhimeow.mhdftools.bukkit.module.core.message.PlayerTeleportMessage;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import org.jetbrains.annotations.NotNull;

@ModulePriority(2000)
public final class ModuleMain extends Module {
    public static ModuleMain instance;
    public final ServerTeleportCache serverTeleportCache = new ServerTeleportCache();

    public ModuleMain() {
        super("core");
        ModuleMain.instance = this;
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
    public void onLoad() {
        MHDFToolsBukkit.getInstance().getRedisManager().register(PlayerMessage.ID, PlayerMessage.CODEC);
        MHDFToolsBukkit.getInstance().getRedisManager().register(PlayerTeleportMessage.ID, PlayerTeleportMessage.CODEC);
    }

    @Override
    public void onEnable() {
        this.serverTeleportCache.init();
    }

    @Override
    public void onDisable() {
        this.serverTeleportCache.close();
        ServerTeleport.close();
    }

    @Override
    public @NotNull AbstractYamlSetting<ConfigSetting.Config> getConfig() {
        return ConfigSetting.getInstance();
    }
}
