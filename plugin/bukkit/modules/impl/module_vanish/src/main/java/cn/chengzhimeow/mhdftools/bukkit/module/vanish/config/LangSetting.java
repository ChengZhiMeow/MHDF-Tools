package cn.chengzhimeow.mhdftools.bukkit.module.vanish.config;

import cn.chengzhimeow.mhdftools.bukkit.module.vanish.ModuleMain;
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
                new Config.Bossbar(
                        super.component("bossbar.vanish", prefix)
                ),
                new Config.Commands(
                        new Config.Commands.Vanish(
                                super.component("commands.vanish.usage", prefix),
                                super.component("commands.vanish.description", prefix),
                                super.component("commands.vanish.message", prefix)
                        )
                )
        );
    }

    public record Config(
            Bossbar bossbar,
            Commands commands
    ) {
        public record Bossbar(
                TextComponent vanish
        ) {
        }

        public record Commands(
                Vanish vanish
        ) {
            public record Vanish(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent message
            ) {
            }
        }
    }
}
