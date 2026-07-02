package cn.chengzhimeow.mhdftools.bukkit.module.bungee;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.ModulePriority;
import cn.chengzhimeow.mhdftools.bukkit.module.bungee.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.bungee.manager.BungeeCordManagerImpl;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

@ModulePriority(1000)
public final class ModuleMain extends Module {
    public static ModuleMain instance;

    public ModuleMain() {
        super("bungee");
        ModuleMain.instance = this;
    }

    @Override
    public void onLoad() {
        BungeeCordManager.setInstance(BungeeCordManagerImpl.getInstance());
    }

    @Override
    public void onEnable() {
        if (!ConfigSetting.getInstance().getConfig().enable()) return;

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(MHDFToolsBukkit.getInstance(), "BungeeCord");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(MHDFToolsBukkit.getInstance(), "BungeeCord", BungeeCordManagerImpl.getInstance().getListener());
    }

    @Override
    public void onDisable() {
        if (!ConfigSetting.getInstance().getConfig().enable()) return;

        Bukkit.getServer().getMessenger().unregisterOutgoingPluginChannel(MHDFToolsBukkit.getInstance(), "BungeeCord");
        Bukkit.getServer().getMessenger().unregisterIncomingPluginChannel(MHDFToolsBukkit.getInstance(), "BungeeCord");
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
