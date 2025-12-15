package cn.chengzhimeow.mhdftools.bukkit;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsAPIImpl;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.config.ConfigsManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.BStatsManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.LogFilterManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.PluginHookManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.cache.CacheManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.cache.MHDFCacheManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.database.MHDFDatabaseManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.feature.CommandManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.feature.ListenerManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.feature.TaskManager;
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

    private MHDFDatabaseManager databaseManager;
    private CacheManager cacheManager;
    private PluginHookManager pluginHookManager;

    private CommandManager commandManager;
    private ListenerManager listenerManager;
    private TaskManager taskManager;

    private BungeeCordManager bungeeCordManager;
    private BStatsManager bStatsManager;

    @Override
    @SneakyThrows
    public void onLoad() {
        MHDFToolsBukkit.setInstance(this);

        // noinspection deprecation
        PluginManager.getInstance().version = this.getDescription().getVersion();
        PluginManager.getInstance().minecraftVersion = Integer.parseInt(Bukkit.getMinecraftVersion().replace(".", ""));
        PluginManager.getInstance().serverType = ServerType.BUKKIT;

        ConfigManager.getInstance().setDataFolder(this.getDataFolder());
        ConfigManager.getInstance().init();

        LibraryManager.getInstance().setLoggerManager(new LoggerManager() {
            @Override
            public void log(String s) {
                Main.this.getLogger().info(s);
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

        ConfigsManager.getInstance().saveDefaultFiles();
        ConfigsManager.getInstance().updateAll();
        ConfigsManager.getInstance().reloadAll();

        LogFilterManager logFilterManager = new LogFilterManager();
        logFilterManager.init();
    }

    @Override
    public void onEnable() {
        this.databaseManager = new MHDFDatabaseManager();
        this.databaseManager.connect();
        this.databaseManager.initTable();

        this.cacheManager = new MHDFCacheManager().getCacheManager();
        this.cacheManager.init();

        this.pluginHookManager = new PluginHookManager();
        this.pluginHookManager.hook();

        this.commandManager = new CommandManager();
        this.commandManager.init();

        this.listenerManager = new ListenerManager();
        this.listenerManager.init();

        this.taskManager = new TaskManager();
        this.taskManager.init();

        this.bungeeCordManager = new BungeeCordManager();
        this.bungeeCordManager.init();

        this.bStatsManager = new BStatsManager();
        this.bStatsManager.init();

        MHDFToolsAPI.setInstance(new MHDFToolsAPIImpl());

        LogManager.instance.log("&e-----------&6=&e梦之工具&6=&e-----------");
        LogManager.instance.log("&a插件启动成功! 官方交流群: 129139830");
        LogManager.instance.log("&e-----------&6=&e梦之工具&6=&e-----------");
    }

    @Override
    public void onDisable() {
        if (this.bungeeCordManager != null) this.bungeeCordManager.close();
        if (this.pluginHookManager != null) this.pluginHookManager.unhook();
        if (this.cacheManager != null) this.cacheManager.close();
        if (this.databaseManager != null) this.databaseManager.close();

        LogManager.instance.log("&e-----------&6=&e梦之工具&6=&e-----------");
        LogManager.instance.log("&a插件卸载成功! 官方交流群: 129139830");
        LogManager.instance.log("&e-----------&6=&e梦之工具&6=&e-----------");
    }
}
