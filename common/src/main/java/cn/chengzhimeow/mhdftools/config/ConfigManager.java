package cn.chengzhimeow.mhdftools.config;

import cn.chengzhimeow.ccyaml.CCYaml;
import cn.chengzhimeow.mhdftools.config.impl.LibrarySetting;
import cn.chengzhimeow.mhdftools.config.impl.ProxySetting;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;

@Getter
public final class ConfigManager {
    private static ConfigManager instance;

    public static ConfigManager getInstance() {
        if (ConfigManager.instance == null) ConfigManager.instance = new ConfigManager();
        return ConfigManager.instance;
    }

    @Setter
    private File dataFolder;
    private CCYaml yamlManager;

    @SneakyThrows
    public void init() {
        if (!this.dataFolder.exists()) Files.createDirectories(this.dataFolder.toPath());
        this.yamlManager = new CCYaml(
                ConfigManager.class.getClassLoader(),
                this.dataFolder,
                PluginManager.getInstance().version
        );

        LibrarySetting.getInstance().saveDefaultFile();
        LibrarySetting.getInstance().update();
        LibrarySetting.getInstance().reload();

        ProxySetting.getInstance().saveDefaultFile();
        ProxySetting.getInstance().update();
        ProxySetting.getInstance().reload();
    }
}
