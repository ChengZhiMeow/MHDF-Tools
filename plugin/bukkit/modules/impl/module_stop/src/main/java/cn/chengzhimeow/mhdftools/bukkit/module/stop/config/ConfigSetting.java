package cn.chengzhimeow.mhdftools.bukkit.module.stop.config;

import cn.chengzhimeow.mhdftools.bukkit.module.stop.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

import java.util.List;

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
                super.getData().getBoolean("confirm"),
                super.getData().getBoolean("auto_save"),
                new Config.Countdown(
                        super.getData().getInt("countdown.default"),
                        super.getData().getIntList("countdown.show_message_time")
                ),
                super.getData().getStringList("commands")
        );
    }

    public record Config(
            boolean enable,
            boolean confirm,
            boolean autoSave,
            Countdown countdown,
            List<String> commands
    ) {
        public record Countdown(
                int defaultTime,
                List<Integer> showMessageTime
        ) {
        }
    }
}
