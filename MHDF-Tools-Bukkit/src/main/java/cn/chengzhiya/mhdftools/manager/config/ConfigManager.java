package cn.chengzhiya.mhdftools.manager.config;

import cn.chengzhiya.mhdftools.util.config.*;
import lombok.SneakyThrows;

@SuppressWarnings("unused")
public final class ConfigManager {
    /**
     * 初始化配置文件
     */
    public void init() {
        saveDefaultAll();
        reloadAll();
    }

    /**
     * 保存所有默认配置文件
     */
    @SneakyThrows
    public void saveDefaultAll() {
        ConfigUtil.saveDefaultConfig();
        LangUtil.saveDefaultLang();
        SoundUtil.saveDefaultSound();
        CustomMenuConfigUtil.saveDefaultCustomMenu();
        MenuConfigUtil.saveDefaultMenu();
    }

    /**
     * 重载所有配置文件
     */
    public void reloadAll() {
        ConfigUtil.reloadConfig();
        LangUtil.reloadLang();
        SoundUtil.reloadSound();
        CustomMenuConfigUtil.reloadCustomMenu();
        MenuConfigUtil.reloadMenu();
    }
}
