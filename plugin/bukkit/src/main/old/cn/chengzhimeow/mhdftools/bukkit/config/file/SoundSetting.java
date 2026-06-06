package cn.chengzhimeow.mhdftools.bukkit.config.file;

import cn.chengzhimeow.ccyaml.manager.AbstractYamlManager;
import cn.chengzhimeow.mhdftools.config.ConfigManager;

public final class SoundSetting extends AbstractYamlManager {
    private static SoundSetting instance;

    public static SoundSetting getInstance() {
        if (SoundSetting.instance == null) {
            SoundSetting.instance = new SoundSetting();
        }
        return SoundSetting.instance;
    }

    private SoundSetting() {
        super(ConfigManager.getInstance().getYamlManager());
    }

    @Override
    public String originFilePath() {
        return "sound_zh.yml";
    }

    @Override
    public String filePath() {
        return "sound.yml";
    }
}
