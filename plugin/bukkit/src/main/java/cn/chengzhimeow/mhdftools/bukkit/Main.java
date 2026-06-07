package cn.chengzhimeow.mhdftools.bukkit;

import cn.chengzhimeow.ccyaml.CCYaml;
import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsAPIImpl;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.api.cache.CacheManager;
import cn.chengzhimeow.mhdftools.bukkit.api.cache.CacheManagerImpl;
import cn.chengzhimeow.mhdftools.bukkit.api.database.DatabaseManager;
import cn.chengzhimeow.mhdftools.bukkit.api.manager.ItemManager;
import cn.chengzhimeow.mhdftools.bukkit.api.manager.ItemManagerImpl;
import cn.chengzhimeow.mhdftools.bukkit.api.message.PlayerMessage;
import cn.chengzhimeow.mhdftools.bukkit.api.redis.RedisManagerImpl;
import cn.chengzhimeow.mhdftools.bukkit.common.config.CacheSetting;
import cn.chengzhimeow.mhdftools.bukkit.common.config.DatabaseSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.menu.listener.MenuListener;
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
    private CacheManagerImpl cacheManager;
    private RedisManagerImpl redisManager;
    private ItemManager itemManager;

    @Override
    @SneakyThrows
    public void onLoad() {
        Main.instance = this;
        MHDFToolsBukkit.setInstance(this);

        PluginManager.getInstance().version = this.getDescription().getVersion();
        PluginManager.getInstance().minecraftVersion = Integer.parseInt(Bukkit.getMinecraftVersion().replace(".", ""));
        PluginManager.getInstance().serverType = ServerType.BUKKIT;

        ConfigManager.getInstance().setDataFolder(this.getDataFolder());
        ConfigManager.getInstance().setYamlManager(new CCYaml(this.getClassLoader(), this.getDataFolder(), PluginManager.getInstance().version));
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

        CacheSetting.getInstance().saveDefaultFile();
        CacheSetting.getInstance().update();
        CacheSetting.getInstance().reload();

        DatabaseSetting.getInstance().saveDefaultFile();
        DatabaseSetting.getInstance().update();
        DatabaseSetting.getInstance().reload();
    }

    @Override
    public CacheManager getCacheManager() {
        return this.cacheManager;
    }

    @Override
    public void onEnable() {
        this.cacheManager = new CacheManagerImpl();

        this.databaseManager = new DatabaseManager(this, ConfigSetting.getInstance().getData());
        this.databaseManager.connect();
        this.databaseManager.initTable();

        this.redisManager = new RedisManagerImpl();
        this.redisManager.configure();
        this.redisManager.register(PlayerMessage.ID, PlayerMessage.CODEC);

        this.itemManager = new ItemManagerImpl();

        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);

        MHDFToolsAPI.setInstance(new MHDFToolsAPIImpl(this.databaseManager));
        LogManager.instance.log("&aMHDF-Tools enabled.");
    }

    @Override
    public void onDisable() {
        if (this.databaseManager != null) this.databaseManager.close();
        if (this.redisManager != null) this.redisManager.close();
        if (this.cacheManager != null) this.cacheManager.close();

        MHDFToolsAPI.setInstance(null);
        LogManager.instance.log("&aMHDF-Tools disabled.");
    }
}
