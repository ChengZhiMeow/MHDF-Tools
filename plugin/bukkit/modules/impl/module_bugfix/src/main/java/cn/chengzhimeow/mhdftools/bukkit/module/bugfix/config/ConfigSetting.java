package cn.chengzhimeow.mhdftools.bukkit.module.bugfix.config;

import cn.chengzhimeow.mhdftools.bukkit.module.bugfix.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

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
                new Config.Dupe(
                        new Config.Dupe.Trident(
                                super.getData().getBoolean("dupe.trident.enable")
                        )
                ),
                new Config.Crash(
                        new Config.Crash.Bundle(
                                super.getData().getBoolean("crash.bundle.enable")
                        )
                )
        );
    }

    public record Config(
            Dupe dupe,
            Crash crash
    ) {
        public record Dupe(
                Trident trident
        ) {
            public record Trident(
                    boolean enable
            ) {
            }
        }

        public record Crash(
                Bundle bundle
        ) {
            public record Bundle(
                    boolean enable
            ) {
            }
        }
    }
}
