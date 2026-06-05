package cn.chengzhimeow.mhdftools.bukkit.module.knockback.config;

import cn.chengzhimeow.mhdftools.bukkit.module.knockback.ModuleMain;
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
                        new Config.Commands.Knockback(
                                super.component("commands.knockback.usage", prefix),
                                super.component("commands.knockback.description", prefix),
                                super.component("commands.knockback.no_type", prefix),
                                super.component("commands.knockback.message", prefix),
                                new Config.Commands.Knockback.Types(
                                        super.component("commands.knockback.types.normal", prefix),
                                        super.component("commands.knockback.types.random", prefix)
                                )
                        )
                )
        );
    }

    public record Config(
            Commands commands
    ) {
        public record Commands(
                Knockback knockback
        ) {
            public record Knockback(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent noType,
                    TextComponent message,
                    Types types
            ) {
                public record Types(
                        TextComponent normal,
                        TextComponent random
                ) {
                }
            }
        }
    }
}
