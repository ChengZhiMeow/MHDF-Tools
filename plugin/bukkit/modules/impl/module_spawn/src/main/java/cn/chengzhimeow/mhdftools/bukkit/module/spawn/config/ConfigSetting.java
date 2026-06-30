package cn.chengzhimeow.mhdftools.bukkit.module.spawn.config;

import cn.chengzhimeow.mhdftools.bukkit.module.spawn.ModuleMain;
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
                new Config.AutoTeleport(
                        super.getData().getBoolean("auto_teleport.respawn"),
                        super.getData().getBoolean("auto_teleport.join")
                ),
                super.getData().getStringList("spawn_commands"),
                super.getData().getStringList("setspawn_commands"),
                super.getData().getStringList("black_world")
        );
    }

    public record Config(
            boolean enable,
            AutoTeleport autoTeleport,
            List<String> spawnCommands,
            List<String> setspawnCommands,
            List<String> blackWorld
    ) {
        public record AutoTeleport(
                boolean respawn,
                boolean join
        ) {
        }
    }
}
