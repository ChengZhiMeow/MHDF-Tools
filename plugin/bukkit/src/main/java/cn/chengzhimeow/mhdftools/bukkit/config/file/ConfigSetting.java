package cn.chengzhimeow.mhdftools.bukkit.config.file;

import cn.chengzhimeow.ccyaml.manager.AbstractYamlManager;
import cn.chengzhimeow.mhdftools.config.ConfigManager;

public final class ConfigSetting extends AbstractYamlManager {
    private static ConfigSetting instance;

    public static ConfigSetting getSettingInstance() {
        if (ConfigSetting.instance == null) {
            ConfigSetting.instance = new ConfigSetting();
        }
        return ConfigSetting.instance;
    }

    private ConfigSetting() {
        super(ConfigManager.getInstance().getYamlManager());
    }

    @Override
    public String originFilePath() {
        return "config_zh.yml";
    }

    @Override
    public String filePath() {
        return "config.yml";
    }
}
