package cn.chengzhiya.mhdftools;

import cn.chengzhiya.mhdftools.exception.FileException;
import cn.chengzhiya.mhdftools.exception.ResourceException;
import cn.chengzhiya.mhdftools.manager.*;
import cn.chengzhiya.mhdftools.manager.config.ConfigManager;
import cn.chengzhiya.mhdftools.manager.database.MHDFDatabaseManager;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.FileUtil;
import cn.chengzhiya.mhdftools.util.config.ProxyUtil;
import cn.chengzhiya.mhdftools.util.message.LogUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {
    public static Main instance;

    private boolean nativeSupportAdventureApi;

    private ConfigManager configManager;
    private LibrariesManager librariesManager;
    private MinecraftLangManager minecraftLangManager;
    private LogFilterManager logFilterManager;

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

        // 释放代理配置
        {
            FileUtil.createFolder(ConfigUtil.getDataFolder());

            ProxyUtil.saveDefaultProxy();
            ProxyUtil.reloadProxy();
        }

        try {
            Class.forName("net.kyori.adventure.text.Component");
            Player.class.getDeclaredMethod("displayName");
            nativeSupportAdventureApi = true;
        } catch (NoSuchMethodError | ClassNotFoundException | NoSuchMethodException e) {
            nativeSupportAdventureApi = false;
        }

        this.librariesManager = new LibrariesManager();
        this.librariesManager.init();

        this.configManager = new ConfigManager();
        this.configManager.init();

        this.minecraftLangManager = new MinecraftLangManager();
        this.minecraftLangManager.init();

        this.logFilterManager = new LogFilterManager();
        this.logFilterManager.init();
    }

    @Override
    public void onEnable() {
        this.adventureManager = new AdventureManager();
        this.adventureManager.init();

        this.databaseManager = new MHDFDatabaseManager();
        this.databaseManager.connect();
        this.databaseManager.initTable();

        this.cacheManager = new CacheManager();
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
        if (!isNativeSupportAdventureApi()) {
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
        this.logFilterManager = null;
        this.minecraftLangManager = null;
        this.librariesManager = null;
        this.configManager = null;

        instance = null;
    }
}
