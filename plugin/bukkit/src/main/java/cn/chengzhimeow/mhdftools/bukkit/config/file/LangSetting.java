package cn.chengzhimeow.mhdftools.bukkit.config.file;

import cn.chengzhimeow.mhdftools.bukkit.module.config.ModuleLangSetting;

public final class LangSetting extends ModuleLangSetting {
    private static LangSetting instance;

    public static LangSetting getSettingInstance() {
        if (LangSetting.instance == null) {
            LangSetting.instance = new LangSetting();
        }
        return LangSetting.instance;
    }

    @Override
    public String originFilePath() {
        return "lang_zh.yml";
    }

    @Override
    public String filePath() {
        return "lang.yml";
    }
}
