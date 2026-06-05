package cn.chengzhimeow.mhdftools.bukkit.module.list.config;

import cn.chengzhimeow.mhdftools.bukkit.module.list.ModuleMain;
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
                super.getData().getBoolean("get_player_list_from_bungeecord"),
                super.getData().getStringList("commands")
        );
    }

    public record Config(
            boolean enable,
            boolean getPlayerListFromBungeecord,
            List<String> commands
    ) {
    }
}
