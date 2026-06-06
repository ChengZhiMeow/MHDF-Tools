package cn.chengzhimeow.mhdftools.bukkit;

import cn.chengzhimeow.ccyaml.CCYaml;
import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsAPIImpl;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.api.database.DatabaseManager;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.config.ConfigManager;
import cn.chengzhimeow.mhdftools.console.LogManager;
import cn.chengzhimeow.mhdftools.library.LibraryManager;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.message.StringUtil;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;
import cn.chengzhimeow.mhdftools.plugin.ServerType;
import cn.chengzhiya.mhdflibrary.manager.LoggerManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

@Getter
public final class Main extends MHDFToolsBukkit {
    public static Main instance;

    private DatabaseManager databaseManager;

    @Override
    @SneakyThrows
    public void onLoad() {
        Main.instance = this;
        MHDFToolsBukkit.setInstance(this);

        PluginManager.getInstance().version = this.getDescription().getVersion();
        PluginManager.getInstance().minecraftVersion = Integer.parseInt(Bukkit.getMinecraftVersion().replace(".", ""));
        PluginManager.getInstance().serverType = ServerType.BUKKIT;

        ConfigManager.getInstance().setDataFolder(this.getDataFolder());
        ConfigManager.getInstance().setYamlManager(new CCYaml(this.getClassLoader(), this.getDataFolder(), this.getDescription().getVersion()));
        ConfigManager.getInstance().init();

        LibraryManager.getInstance().setLoggerManager(new LoggerManager() {
            @Override
            public void log(String message) {
                Main.this.getLogger().info(message);
            }
        });
        LibraryManager.getInstance().init();

        LogManager.instance = new LogManager() {
            @Override
            public void log(String message, String... args) {
                Bukkit.getConsoleSender().sendMessage(ColorUtil.color(LogManager.CONSOLE_PREFIX + StringUtil.format(message, args)));
            }

            @Override
            public void debug(String message, String... args) {
                Bukkit.getConsoleSender().sendMessage(ColorUtil.color(LogManager.DEBUG_PREFIX + StringUtil.format(message, args)));
            }
        };

        ConfigSetting.getInstance().saveDefaultFile();
        ConfigSetting.getInstance().update();
        ConfigSetting.getInstance().reload();
    }

    @Override
    public void onEnable() {
        this.databaseManager = new DatabaseManager(this, ConfigSetting.getInstance().getData());
        this.databaseManager.connect();
        this.databaseManager.initTable();

        MHDFToolsAPI.setInstance(new MHDFToolsAPIImpl(this.databaseManager));
        LogManager.instance.log("&aMHDF-Tools enabled.");
    }

    @Override
    public void onDisable() {
        if (this.databaseManager != null) {
            this.databaseManager.close();
        }
        MHDFToolsAPI.setInstance(null);
        LogManager.instance.log("&aMHDF-Tools disabled.");
    }
}
