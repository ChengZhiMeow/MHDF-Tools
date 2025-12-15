package cn.chengzhimeow.mhdftools.bungee;

import cn.chengzhimeow.ccyaml.CCYaml;
import cn.chengzhimeow.mhdftools.bungee.listener.PluginMessage;
import cn.chengzhimeow.mhdftools.config.ConfigManager;
import cn.chengzhimeow.mhdftools.console.LogManager;
import cn.chengzhimeow.mhdftools.library.LibraryManager;
import cn.chengzhimeow.mhdftools.message.StringUtil;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;
import cn.chengzhimeow.mhdftools.plugin.ServerType;
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
    public void onLoad() {
        LogManager.instance = new LogManager() {
            @Override
            public void log(String message, String... args) {
                Main.this.getLogger().info(LogManager.CONSOLE_PREFIX + StringUtil.format(message, args));
            }

            @Override
            public void debug(String message, String... args) {
                Main.this.getLogger().info(LogManager.DEBUG_PREFIX + StringUtil.format(message, args));
            }
        };
    }

    @Override
    public void onEnable() {
        Main.instance = this;

        PluginManager.getInstance().version = this.getDescription().getVersion();
        PluginManager.getInstance().minecraftVersion = this.getGameVersion();
        PluginManager.getInstance().serverType = ServerType.BUNGEE;

        ConfigManager.getInstance().setDataFolder(this.getDataFolder());
        ConfigManager.getInstance().setYamlManager(new CCYaml(
                ConfigManager.class.getClassLoader(),
                this.getDataFolder(),
                PluginManager.getInstance().version
        ));
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
