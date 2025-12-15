package cn.chengzhimeow.mhdftools.velocity;

import cn.chengzhimeow.ccyaml.CCYaml;
import cn.chengzhimeow.mhdftools.config.ConfigManager;
import cn.chengzhimeow.mhdftools.console.LogManager;
import cn.chengzhimeow.mhdftools.library.LibraryManager;
import cn.chengzhimeow.mhdftools.message.StringUtil;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;
import cn.chengzhimeow.mhdftools.plugin.ServerType;
import cn.chengzhimeow.mhdftools.velocity.listener.PluginMessage;
import cn.chengzhiya.mhdflibrary.manager.LoggerManager;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

@Getter
public final class Main {
    public static Main instance;
    private final File dataFolder;
    @Inject
    private ProxyServer server;
    @Inject
    private Logger logger;
    @Inject
    private PluginContainer pluginContainer;

    @Inject
    public Main(@DataDirectory Path dataPath) {
        this.dataFolder = dataPath.toFile();

        LogManager.instance = new LogManager() {
            @Override
            public void log(String message, String... args) {
                // noinspection StringConcatenationArgumentToLogCall
                Main.this.getLogger().info(LogManager.CONSOLE_PREFIX + StringUtil.format(message, args));
            }

            @Override
            public void debug(String message, String... args) {
                // noinspection StringConcatenationArgumentToLogCall
                Main.this.getLogger().info(LogManager.DEBUG_PREFIX + StringUtil.format(message, args));
            }
        };
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        Main.instance = this;

        PluginManager.getInstance().version = this.pluginContainer.getDescription().getVersion().toString();
        PluginManager.getInstance().minecraftVersion = Integer.parseInt(ProtocolVersion.MAXIMUM_VERSION.getMostRecentSupportedVersion().replace(".", ""));
        PluginManager.getInstance().serverType = ServerType.VELOCITY;

        ConfigManager.getInstance().setDataFolder(this.dataFolder);
        ConfigManager.getInstance().setYamlManager(new CCYaml(
                ConfigManager.class.getClassLoader(),
                this.dataFolder,
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

        this.server.getChannelRegistrar().register(PluginMessage.IDENTIFIER);
    }
}
