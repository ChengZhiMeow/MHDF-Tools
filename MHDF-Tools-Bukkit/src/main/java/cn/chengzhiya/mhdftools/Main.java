package cn.chengzhiya.mhdftools;

import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIImpl;
import cn.chengzhiya.mhdftools.manager.*;
import cn.chengzhiya.mhdftools.manager.cache.CacheManager;
import cn.chengzhiya.mhdftools.manager.cache.MHDFCacheManager;
import cn.chengzhiya.mhdftools.manager.config.ConfigFolderManager;
import cn.chengzhiya.mhdftools.manager.config.ConfigManager;
import cn.chengzhiya.mhdftools.manager.config.ProxyConfigManager;
import cn.chengzhiya.mhdftools.manager.database.MHDFDatabaseManager;
import cn.chengzhiya.mhdftools.manager.feature.CommandManager;
import cn.chengzhiya.mhdftools.manager.feature.ListenerManager;
import cn.chengzhiya.mhdftools.manager.feature.TaskManager;
import cn.chengzhiya.mhdftools.util.PluginUtil;
import cn.chengzhiya.mhdftools.util.message.LogUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {
    public static Main instance;

    private LibrariesManager librariesManager;
    private ConfigManager configManager;
    private MinecraftLangManager minecraftLangManager;

    private AdventureManager adventureManager;
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
        instance = this;

        ConfigFolderManager configFolderManager = new ConfigFolderManager();
        configFolderManager.init();

        ProxyConfigManager proxyConfigManager = new ProxyConfigManager();
        proxyConfigManager.init();

        this.librariesManager = new LibrariesManager();
        this.librariesManager.init();

        this.configManager = new ConfigManager();
        this.configManager.init();

        this.minecraftLangManager = new MinecraftLangManager();
        this.minecraftLangManager.init();

        LogFilterManager logFilterManager = new LogFilterManager();
        logFilterManager.init();
    }

    @Override
    public void onEnable() {
        this.adventureManager = new AdventureManager();
        this.adventureManager.init();

        MHDFToolsAPIHelper.setInstance(new MHDFToolsAPIImpl());

        this.databaseManager = new MHDFDatabaseManager();
        this.databaseManager.connect();
        this.databaseManager.initTable();

        this.cacheManager = new MHDFCacheManager();
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

        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");
        if (!PluginUtil.isNativeSupportAdventureApi()) {
            LogUtil.log("&c警告! 插件正在使用无服务端原生支持AdventureAPI兼容模式运行!");
        }
        LogUtil.log("&a插件启动成功! 官方交流群: 129139830");
        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");
    }

    @Override
    public void onDisable() {
        if (this.bungeeCordManager != null) {
            this.bungeeCordManager.close();
        }
        if (this.pluginHookManager != null) {
            this.pluginHookManager.unhook();
        }
        if (this.cacheManager != null) {
            this.cacheManager.close();
        }
        if (this.databaseManager != null) {
            this.databaseManager.close();
        }

        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");
        LogUtil.log("&a插件卸载成功! 官方交流群: 129139830");
        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");

        if (this.adventureManager != null) {
            this.adventureManager.close();
        }

        this.bStatsManager = null;
        this.bungeeCordManager = null;
        this.taskManager = null;
        this.listenerManager = null;
        this.commandManager = null;
        this.pluginHookManager = null;
        this.cacheManager = null;
        this.databaseManager = null;
        this.adventureManager = null;
        this.minecraftLangManager = null;
        this.librariesManager = null;
        this.configManager = null;

        instance = null;
    }
}
