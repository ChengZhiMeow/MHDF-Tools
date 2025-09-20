package cn.chengzhimeow.mhdftools.bukkit;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsAPIImpl;
import cn.chengzhimeow.mhdftools.bukkit.config.ConfigsManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.*;
import cn.chengzhimeow.mhdftools.bukkit.manager.cache.CacheManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.cache.MHDFCacheManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.database.MHDFDatabaseManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.feature.CommandManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.feature.ListenerManager;
import cn.chengzhimeow.mhdftools.bukkit.manager.feature.TaskManager;
import cn.chengzhimeow.mhdftools.bukkit.util.message.LogUtil;
import cn.chengzhimeow.mhdftools.config.ConfigManager;
import cn.chengzhimeow.mhdftools.enums.ServerType;
import cn.chengzhimeow.mhdftools.manager.LibraryManager;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;
import cn.chengzhiya.mhdflibrary.manager.LoggerManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {
    public static Main instance;

    private MinecraftLangManager minecraftLangManager;

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
        Main.instance = this;

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

        ConfigsManager.getInstance().saveDefaultFiles();
        ConfigsManager.getInstance().updateAll();
        ConfigsManager.getInstance().reloadAll();

        this.minecraftLangManager = new MinecraftLangManager();
        this.minecraftLangManager.init();

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

        MHDFToolsAPIHelper.setInstance(new MHDFToolsAPIImpl());

        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");
        LogUtil.log("&a插件启动成功! 官方交流群: 129139830");
        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");
    }

    @Override
    public void onDisable() {
        if (this.bungeeCordManager != null) this.bungeeCordManager.close();
        if (this.pluginHookManager != null) this.pluginHookManager.unhook();
        if (this.cacheManager != null) this.cacheManager.close();
        if (this.databaseManager != null) this.databaseManager.close();

        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");
        LogUtil.log("&a插件卸载成功! 官方交流群: 129139830");
        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");
    }
}
