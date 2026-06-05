package cn.chengzhimeow.mhdftools.bukkit.module.knockback.config;

import cn.chengzhimeow.mhdftools.bukkit.module.knockback.ModuleMain;
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
                new Config.Vector(
                        super.getData().getDouble("vector.x"),
                        super.getData().getDouble("vector.y"),
                        super.getData().getDouble("vector.z")
                ),
                super.getData().getStringList("commands")
        );
    }

    public record Config(
            boolean enable,
            String defaultType,
            Vector vector,
            List<String> commands
    ) {
        public record Vector(
                double x,
                double y,
                double z
        ) {
        }
    }
}
