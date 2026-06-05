package cn.chengzhimeow.mhdftools.bukkit.module.crash.config;

import cn.chengzhimeow.mhdftools.bukkit.module.crash.ModuleMain;
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
                        new Config.Commands.Crash(
                                super.component("commands.crash.usage", prefix),
                                super.component("commands.crash.description", prefix),
                                super.component("commands.crash.no_type", prefix),
                                super.component("commands.crash.message", prefix),
                                new Config.Commands.Crash.Types(
                                        super.component("commands.crash.types.explosion", prefix),
                                        super.component("commands.crash.types.invalid_teleport", prefix),
                                        super.component("commands.crash.types.invalid_particle", prefix)
                                )
                        )
                )
        );
    }

    public record Config(
            Commands commands
    ) {
        public record Commands(
                Crash crash
        ) {
            public record Crash(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent noType,
                    TextComponent message,
                    Types types
            ) {
                public record Types(
                        TextComponent explosion,
                        TextComponent invalidTeleport,
                        TextComponent invalidParticle
                ) {
                }
            }
        }
    }
}
