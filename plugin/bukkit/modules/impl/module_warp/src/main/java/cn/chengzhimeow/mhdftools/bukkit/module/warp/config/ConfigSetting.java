package cn.chengzhimeow.mhdftools.bukkit.module.warp.config;

import cn.chengzhimeow.mhdftools.bukkit.module.warp.ModuleMain;
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
                super.getData().getStringList("black_world"),
                super.getData().getStringList("warp_commands"),
                super.getData().getStringList("setwarp_commands"),
                super.getData().getStringList("delwarp_commands")
        );
    }

    public record Config(
            boolean enable,
            List<String> blackWorld,
            List<String> warpCommands,
            List<String> setWarpCommands,
            List<String> delWarpCommands
    ) {
    }
}
