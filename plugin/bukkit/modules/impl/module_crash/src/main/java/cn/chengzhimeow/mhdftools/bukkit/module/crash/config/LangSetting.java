package cn.chengzhimeow.mhdftools.bukkit.module.crash.config;

import cn.chengzhimeow.mhdftools.bukkit.module.crash.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractLangSetting;
import lombok.Getter;

public final class LangSetting extends AbstractLangSetting {
    @Getter(lazy = true)
    private static final LangSetting instance = new LangSetting();

    @Override
    public String originFilePath() {
        return "module/"+ ModuleMain.instance.getId() +"/lang.yml";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }

    private LangSetting() {
    }
}
