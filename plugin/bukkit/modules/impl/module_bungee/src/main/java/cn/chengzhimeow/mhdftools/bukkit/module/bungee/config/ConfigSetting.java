package cn.chengzhimeow.mhdftools.bukkit.module.bungee.config;

import cn.chengzhimeow.mhdftools.bukkit.module.bungee.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

public final class ConfigSetting extends AbstractYamlSetting<ConfigSetting.Config> {
    @Getter(lazy = true)
    private static final ConfigSetting instance = new ConfigSetting();
    @Getter private Config config;

    private ConfigSetting() {
    }

    @Override
    public String originFilePath() {
        return "module/" + ModuleMain.instance.getId() + "/config.yml";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }

    @Override
    public void reload() {
        super.reload();

        this.config = new Config(
                super.getData().getBoolean("enable"),
                new Config.AutoTry(
                        super.getData().getLong("autoTry.delay"),
                        super.getData().getInt("autoTry.maxTimes")
                )
        );
    }

    public record Config(
            boolean enable,
            AutoTry autoTry
    ) {
        public record AutoTry(
                long delay,
                int maxTimes
        ) {
        }
    }
}
