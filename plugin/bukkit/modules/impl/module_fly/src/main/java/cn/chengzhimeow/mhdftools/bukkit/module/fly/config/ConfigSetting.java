package cn.chengzhimeow.mhdftools.bukkit.module.fly.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.module.fly.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                new Config.AutoEnable(
                        super.getData().getBoolean("auto_enable.join_server"),
                        super.getData().getBoolean("auto_enable.change_world"),
                        super.getData().getBoolean("auto_enable.respawn")
                ),
                new Config.AutoDisable(
                        super.getData().getBoolean("auto_disable.take_health"),
                        super.getData().getStringList("auto_disable.world_list")
                ),
                super.getData().getInt("fall_time"),
                this.fallSounds(),
                super.getData().getStringList("fly_commands"),
                super.getData().getStringList("flytime_commands")
        );
    }

    private Map<Long, Config.FallSound> fallSounds() {
        Map<Long, Config.FallSound> sounds = new HashMap<>();
        ConfigurationSection section = super.getData().getConfigurationSection("fall_sounds");
        if (section == null) return sounds;

        for (String key : section.getKeys(false)) {
            try {
                sounds.put(
                        Long.parseLong(key),
                        new Config.FallSound(
                                section.getString(key + ".sound", ""),
                                section.getFloat(key + ".volume", 1f),
                                section.getFloat(key + ".pitch", 1f)
                        )
                );
            } catch (NumberFormatException ignored) {
            }
        }
        return sounds;
    }

    public record Config(
            boolean enable,
            AutoEnable autoEnable,
            AutoDisable autoDisable,
            int fallTime,
            Map<Long, FallSound> fallSounds,
            List<String> flyCommands,
            List<String> flytimeCommands
    ) {
        public record AutoEnable(
                boolean joinServer,
                boolean changeWorld,
                boolean respawn
        ) {
        }

        public record AutoDisable(
                boolean takeHealth,
                List<String> worldList
        ) {
        }

        public record FallSound(
                String sound,
                float volume,
                float pitch
        ) {
        }
    }
}
