package cn.chengzhimeow.mhdftools.bungee;

import cn.chengzhimeow.mhdftools.bungee.listener.PluginMessage;
import cn.chengzhimeow.mhdftools.config.ConfigManager;
import cn.chengzhimeow.mhdftools.enums.ServerType;
import cn.chengzhimeow.mhdftools.manager.LibraryManager;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;
import cn.chengzhiya.mhdflibrary.manager.LoggerManager;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.protocol.ProtocolConstants;

import java.lang.reflect.Field;

public final class Main extends Plugin {
    public static Main instance;

    private int getGameVersion() {
        int version = -1;
        for (Field field : ProtocolConstants.class.getDeclaredFields()) {
            String name = field.getName();
            if (!name.startsWith("MINECRAFT_")) continue;
            version = Integer.parseInt(name.replace("MINECRAFT_", "").replace("_", ""));
        }
        return version;
    }

    @Override
    public void onEnable() {
        Main.instance = this;

        PluginManager.getInstance().version = this.getDescription().getVersion();
        PluginManager.getInstance().minecraftVersion = this.getGameVersion();
        PluginManager.getInstance().serverType = ServerType.BUNGEE;

        ConfigManager.getInstance().setDataFolder(this.getDataFolder());
        ConfigManager.getInstance().init();

        LibraryManager.getInstance().setLoggerManager(new LoggerManager() {
            @Override
            public void log(String s) {
                Main.this.getLogger().info(s);
            }
        });
        LibraryManager.getInstance().init();

        super.getProxy().getPluginManager().registerListener(this, new PluginMessage());
        super.getProxy().registerChannel("BungeeCord");
    }

    @Override
    public void onDisable() {
        Main.instance = null;
    }
}
