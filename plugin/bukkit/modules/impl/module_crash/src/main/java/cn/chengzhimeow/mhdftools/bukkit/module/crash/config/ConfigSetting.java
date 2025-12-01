package cn.chengzhimeow.mhdftools.bukkit.module.crash.config;

import cn.chengzhimeow.mhdftools.bukkit.module.crash.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

public final class ConfigSetting extends AbstractYamlSetting {
    @Getter(lazy = true)
    private static final ConfigSetting instance = new ConfigSetting();

    @Override
    public String originFilePath() {
        return "module/"+ ModuleMain.instance.getId() +"/config.yml";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }

    private ConfigSetting() {
    }
}
