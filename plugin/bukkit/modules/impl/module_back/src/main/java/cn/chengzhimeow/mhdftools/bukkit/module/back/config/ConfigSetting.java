package cn.chengzhimeow.mhdftools.bukkit.module.back.config;

import cn.chengzhimeow.mhdftools.bukkit.module.back.ModuleMain;
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
                super.getData().getInt("default_max"),
                new Config.Save(
                        super.getData().getBoolean("save.teleport"),
                        super.getData().getBoolean("save.death")
                ),
                super.getData().getBoolean("respawn_message"),
                super.getData().getStringList("black_world"),
                super.getData().getStringList("commands")
        );
    }

    public record Config(
            boolean enable,
            int defaultMax,
            Save save,
            boolean respawnMessage,
            List<String> blackWorld,
            List<String> commands
    ) {
        public record Save(
                boolean teleport,
                boolean death
        ) {
        }
    }
}
