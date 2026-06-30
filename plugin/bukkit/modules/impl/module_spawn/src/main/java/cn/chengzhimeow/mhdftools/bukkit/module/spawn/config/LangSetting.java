package cn.chengzhimeow.mhdftools.bukkit.module.spawn.config;

import cn.chengzhimeow.mhdftools.bukkit.module.spawn.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractLangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import lombok.Getter;

public final class LangSetting extends AbstractLangSetting<LangSetting.Config> {
    @Getter(lazy = true)
    private static final LangSetting instance = new LangSetting();

    private LangSetting() {
    }

    @Override
    public String originFilePath() {
        return "module/" + ModuleMain.instance.getId() + "/lang.yml";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }

    @Override
    public void reload() {
        super.reload();
        TextComponent prefix = GlobalLangSetting.getInstance().getConfig().prefix();

        this.config = new Config(
                new Config.Commands(
                        new Config.Commands.Spawn(
                                super.component("commands.spawn.usage", prefix),
                                super.component("commands.spawn.description", prefix),
                                super.component("commands.spawn.message", prefix)
                        ),
                        new Config.Commands.SetSpawn(
                                super.component("commands.setspawn.usage", prefix),
                                super.component("commands.setspawn.description", prefix),
                                super.component("commands.setspawn.message", prefix)
                        )
                )
        );
    }

    public record Config(
            Commands commands
    ) {
        public record Commands(
                Spawn spawn,
                SetSpawn setspawn
        ) {
            public record Spawn(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent message
            ) {
            }

            public record SetSpawn(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent message
            ) {
            }
        }
    }
}
