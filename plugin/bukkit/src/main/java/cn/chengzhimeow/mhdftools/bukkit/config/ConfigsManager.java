package cn.chengzhimeow.mhdftools.bukkit.config;

import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.SoundSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.folder.CustomMenuManager;
import cn.chengzhimeow.mhdftools.bukkit.config.folder.MenuManager;

public final class ConfigsManager {
    private static ConfigsManager instance;

    public static ConfigsManager getInstance() {
        if (ConfigsManager.instance == null) {
            ConfigsManager.instance = new ConfigsManager();
        }
        return ConfigsManager.instance;
    }

    private ConfigsManager() {
    }

    public void saveDefaultFiles() {
        ConfigSetting.getSettingInstance().saveDefaultFile();
        LangSetting.getSettingInstance().saveDefaultFile();
        SoundSetting.getSettingInstance().saveDefaultFile();

        CustomMenuManager.getSettingInstance().saveDefaultFile();
        MenuManager.getSettingInstance().saveDefaultFile();
    }

    public void updateAll() {
        ConfigSetting.getSettingInstance().update();
        LangSetting.getSettingInstance().update();
        SoundSetting.getSettingInstance().update();
    }

    public void reloadAll() {
        ConfigSetting.getSettingInstance().reload();
        LangSetting.getSettingInstance().reload();
        SoundSetting.getSettingInstance().reload();

        CustomMenuManager.getSettingInstance().reload();
        MenuManager.getSettingInstance().reload();
    }
}
