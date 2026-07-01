package cn.chengzhimeow.mhdftools.bukkit.config;

import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

public final class ConfigSetting extends AbstractYamlSetting<ConfigSetting.Config> {
    @Getter(lazy = true)
    private static final ConfigSetting instance = new ConfigSetting();

    @Override
    public String originFilePath() {
        return "config/bukkit.yml";
    }

    @Override
    public String filePath() {
        return "config.yml";
    }

    @Override
    public void reload() {
        super.reload();

        this.config = new Config(
                super.getData().getBoolean("debug"),
                super.getData().getBoolean("bstats")
        );
    }

    public record Config(
            boolean debug,
            boolean bstats
    ) {
    }
}
