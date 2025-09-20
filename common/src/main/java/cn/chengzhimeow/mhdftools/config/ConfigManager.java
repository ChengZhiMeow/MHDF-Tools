package cn.chengzhimeow.mhdftools.config;

import cn.chengzhimeow.ccyaml.CCYaml;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;

@Getter
public final class ConfigManager {
    private static ConfigManager instance;
    @Setter
    private File dataFolder;
    private CCYaml yamlManager;

    public static ConfigManager getInstance() {
        if (ConfigManager.instance == null) ConfigManager.instance = new ConfigManager();
        return ConfigManager.instance;
    }

    @SneakyThrows
    public void init() {
        if (!this.dataFolder.exists()) Files.createDirectories(this.dataFolder.toPath());
        this.yamlManager = new CCYaml(
                ConfigManager.class.getClassLoader(),
                this.dataFolder,
                PluginManager.getInstance().version
        );

        LibrarySetting.getSettingInstance().saveDefaultFile();
        LibrarySetting.getSettingInstance().update();
        LibrarySetting.getSettingInstance().reload();

        ProxySetting.getSettingInstance().saveDefaultFile();
        ProxySetting.getSettingInstance().update();
        ProxySetting.getSettingInstance().reload();
    }
}
