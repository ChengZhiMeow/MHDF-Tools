package cn.chengzhimeow.mhdftools.bukkit.config;

import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
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
        ConfigSetting.getInstance().saveDefaultFile();
        LangSetting.getInstance().saveDefaultFile();

        CustomMenuManager.getInstance().saveDefaultFile();
        MenuManager.getInstance().saveDefaultFile();
    }

    public void updateAll() {
        ConfigSetting.getInstance().update();
        LangSetting.getInstance().update();
    }

    public void reloadAll() {
        ConfigSetting.getInstance().reload();
        LangSetting.getInstance().reload();

        CustomMenuManager.getInstance().reload();
        MenuManager.getInstance().reload();
    }
}
