package cn.chengzhimeow.mhdftools.bukkit.module.bungee;

import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.ModulePriority;
import cn.chengzhimeow.mhdftools.bukkit.module.bungee.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.bungee.manager.BungeeCordManagerImpl;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

@ModulePriority(Integer.MAX_VALUE)
public final class ModuleMain extends Module {
    public static ModuleMain instance;

    public ModuleMain() {
        super("bungee");
        ModuleMain.instance = this;
    }

    @Override
    public void onLoad() {
        ConfigSetting.getInstance().saveDefaultFile();
        ConfigSetting.getInstance().update();
        ConfigSetting.getInstance().reload();

        BungeeCordManager.setInstance(BungeeCordManagerImpl.getInstance());
    }

    @Override
    public void onEnable() {
        if (!BungeeCordManagerImpl.getInstance().isBungeeCordMode()) return;

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(super.getPlugin(), "BungeeCord");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(super.getPlugin(), "BungeeCord", BungeeCordManagerImpl.getInstance().getListener());
    }

    @Override
    public void onDisable() {
        if (!BungeeCordManagerImpl.getInstance().isBungeeCordMode()) return;

        Bukkit.getServer().getMessenger().unregisterOutgoingPluginChannel(super.getPlugin(), "BungeeCord");
        Bukkit.getServer().getMessenger().unregisterIncomingPluginChannel(super.getPlugin(), "BungeeCord");
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
