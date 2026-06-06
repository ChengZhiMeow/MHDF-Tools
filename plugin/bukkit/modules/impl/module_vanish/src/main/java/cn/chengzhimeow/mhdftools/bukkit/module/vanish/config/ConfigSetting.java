package cn.chengzhimeow.mhdftools.bukkit.module.vanish.config;

import cn.chengzhimeow.mhdftools.bukkit.module.vanish.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;
import net.kyori.adventure.bossbar.BossBar;

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
                super.getData().getStringList("commands"),
                new Config.Bossbar(
                        new Config.Bossbar.Vanish(
                                super.getData().getBoolean("bossbar.vanish.enable"),
                                this.readBossBarColor("bossbar.vanish.color")
                        )
                ),
                super.getData().getBoolean("cancel_entity_target"),
                super.getData().getBoolean("hide_tab_complete"),
                super.getData().getBoolean("open_container_without_animation")
        );
    }

    private BossBar.Color readBossBarColor(String path) {
        try {
            return BossBar.Color.valueOf(super.getData().getString(path, "WHITE").toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return BossBar.Color.WHITE;
        }
    }

    public record Config(
            boolean enable,
            List<String> commands,
            Bossbar bossbar,
            boolean cancelEntityTarget,
            boolean hideTabComplete,
            boolean openContainerWithoutAnimation
    ) {
        public record Bossbar(
                Vanish vanish
        ) {
            public record Vanish(
                    boolean enable,
                    BossBar.Color color
            ) {
            }
        }
    }
}
