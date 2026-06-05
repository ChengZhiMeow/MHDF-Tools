package cn.chengzhimeow.mhdftools.bukkit.module.crash.config;

import cn.chengzhimeow.mhdftools.bukkit.module.crash.ModuleMain;
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
                super.getData().getString("default_type"),
                super.getData().getStringList("commands")
        );
    }

    public record Config(
            boolean enable,
            String defaultType,
            List<String> commands
    ) {
    }
}
