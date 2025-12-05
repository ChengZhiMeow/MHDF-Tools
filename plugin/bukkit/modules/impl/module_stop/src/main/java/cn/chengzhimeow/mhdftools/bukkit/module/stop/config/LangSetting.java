package cn.chengzhimeow.mhdftools.bukkit.module.stop.config;

import cn.chengzhimeow.mhdftools.bukkit.module.stop.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractLangSetting;
import lombok.Getter;

public final class LangSetting extends AbstractLangSetting {
    @Getter(lazy = true)
    private static final LangSetting instance = new LangSetting();

    private LangSetting() {
    }

    @Override
    public String originFilePath() {
        return "module/" + ModuleMain.instance.getId() + "/lang.yml";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }
}
