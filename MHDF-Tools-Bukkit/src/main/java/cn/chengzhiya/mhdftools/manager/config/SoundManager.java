package cn.chengzhiya.mhdftools.manager.config;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdfyaml.manager.YamlManager;

public final class SoundManager extends YamlManager {
    public SoundManager() {
        super(Main.instance.getYamlManager());
    }

    @Override
    public String getOriginFilePath() {
        return "sound_zh.yml";
    }

    @Override
    public String getFilePath() {
        return "sound.yml";
    }
}
