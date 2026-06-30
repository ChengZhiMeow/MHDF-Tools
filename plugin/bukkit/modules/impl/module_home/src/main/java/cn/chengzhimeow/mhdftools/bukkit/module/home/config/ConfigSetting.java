package cn.chengzhimeow.mhdftools.bukkit.module.home.config;

import cn.chengzhimeow.mhdftools.bukkit.module.home.ModuleMain;
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
                super.getData().getBoolean("exist_replace"),
                super.getData().getInt("default_max"),
                super.getData().getString("regex"),
                super.getData().getStringList("black_world"),
                super.getData().getStringList("home_commands"),
                super.getData().getStringList("sethome_commands"),
                super.getData().getStringList("delhome_commands")
        );
    }

    public record Config(
            boolean enable,
            boolean existReplace,
            int defaultMax,
            String regex,
            List<String> blackWorld,
            List<String> homeCommands,
            List<String> setHomeCommands,
            List<String> delHomeCommands
    ) {
    }
}
