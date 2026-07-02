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
import cn.chengzhimeow.mhdftools.bukkit.hook.PacketEventsHook;
import cn.chengzhimeow.mhdftools.bukkit.module.ModuleManager;
import cn.chengzhimeow.mhdftools.config.ConfigManager;
import cn.chengzhimeow.mhdftools.console.LogManager;
import cn.chengzhimeow.mhdftools.library.LibraryManager;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.message.StringUtil;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;
import cn.chengzhimeow.mhdftools.plugin.ServerType;
import cn.chengzhiya.mhdflibrary.manager.LoggerManager;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.bukkit.Bukkit;

@Getter
public final class Main extends MHDFToolsBukkit {
    public static Main instance;

    private DatabaseManager databaseManager;
    private CacheManagerImpl cacheManager;
    private RedisManagerImpl redisManager;
    private ItemManager itemManager;
    private PacketEventsHook packetEventsHook;

    @Override
    public void onLoad() {
        Configurator.setLevel("cn.chengzhimeow.mhdftools.libs.org.reflections", Level.OFF);

        Main.instance = this;
        MHDFToolsBukkit.setInstance(this);

        PluginManager.getInstance().version = this.getDescription().getVersion();
        PluginManager.getInstance().minecraftVersion = Bukkit.getMinecraftVersion();
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
                if (!ConfigSetting.getInstance().getConfig().debug()) return;
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

        ModuleManager.getInstance().initModules();
    }

    @Override
    public CacheManager getCacheManager() {
        return this.cacheManager;
    }

    @Override
    public void onEnable() {
        this.packetEventsHook = new PacketEventsHook();
        this.packetEventsHook.hook();

        this.cacheManager = new CacheManagerImpl();

        this.databaseManager = new DatabaseManager(this);
        this.databaseManager.connect();
        this.databaseManager.initTable();

        this.redisManager = new RedisManagerImpl();
        this.redisManager.configure();
        this.redisManager.register(PlayerMessage.ID, PlayerMessage.CODEC);

        this.itemManager = new ItemManagerImpl();

        MHDFToolsAPI.setInstance(new MHDFToolsAPIImpl(this.databaseManager));

        ModuleManager.getInstance().loadModules();

        LogManager.instance.log("&e===========================================");
        LogManager.instance.log("&eMHDF-Tools | 版本: " + getDescription().getVersion());
        LogManager.instance.log("&eMHDF-Tools | 作者: ChengZhiMeow");
        LogManager.instance.log("");
        LogManager.instance.log("&eCiallo～ (∠·ω< )⌒★");
        LogManager.instance.log("&e\"在意的话，会让眼前的幸福逃走的，傻子才会在意。\"");
        LogManager.instance.log("&e===========================================");
    }

    @Override
    public void onDisable() {
        ModuleManager.getInstance().unloadModules();

        if (this.databaseManager != null) this.databaseManager.close();
        if (this.redisManager != null) this.redisManager.close();
        if (this.cacheManager != null) this.cacheManager.close();
        if (this.packetEventsHook != null) this.packetEventsHook.unhook();

        MHDFToolsAPI.setInstance(null);
    }
}
