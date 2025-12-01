package cn.chengzhimeow.mhdftools.bukkit.config.file;

import cn.chengzhimeow.mhdftools.config.AbstractLangSetting;

public final class LangSetting extends AbstractLangSetting {
    private static LangSetting instance;

    public static LangSetting getInstance() {
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
