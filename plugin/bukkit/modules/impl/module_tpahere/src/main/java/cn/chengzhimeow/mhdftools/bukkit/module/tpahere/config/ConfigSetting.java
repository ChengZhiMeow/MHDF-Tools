package cn.chengzhimeow.mhdftools.bukkit.module.tpahere.config;

import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.ModuleMain;
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
                super.getData().getInt("delay"),
                Math.max(super.getData().getInt("time"), 1),
                super.getData().getStringList("commands"),
                super.getData().getStringList("black_world")
        );
    }

    public record Config(
            boolean enable,
            int delay,
            int time,
            List<String> commands,
            List<String> blackWorld
    ) {
    }
}
