package cn.chengzhimeow.mhdftools.config.impl;

import cn.chengzhimeow.mhdftools.config.AbstractLangSetting;
import cn.chengzhimeow.mhdftools.plugin.PluginManager;
import lombok.Getter;

public final class GlobalLangSetting extends AbstractLangSetting {
    @Getter(lazy = true)
    private static final GlobalLangSetting instance = new GlobalLangSetting();

    @Override
    public String originFilePath() {
        return "lang/" + PluginManager.getInstance().serverType.toString().toLowerCase() + ".yml";
    }

    @Override
    public String filePath() {
        return "lang.yml";
    }

    private GlobalLangSetting() {
    }
}
