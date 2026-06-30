package cn.chengzhimeow.mhdftools.bukkit.module.warp.config;

import cn.chengzhimeow.mhdftools.bukkit.module.warp.ModuleMain;
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
                        new Config.Commands.Warp(
                                super.component("commands.warp.usage", prefix),
                                super.component("commands.warp.description", prefix),
                                super.component("commands.warp.no_warp", prefix),
                                super.component("commands.warp.message", prefix)
                        ),
                        new Config.Commands.SetWarp(
                                super.component("commands.setwarp.usage", prefix),
                                super.component("commands.setwarp.description", prefix),
                                super.component("commands.setwarp.message", prefix)
                        ),
                        new Config.Commands.DelWarp(
                                super.component("commands.delwarp.usage", prefix),
                                super.component("commands.delwarp.description", prefix),
                                super.component("commands.delwarp.no_warp", prefix),
                                super.component("commands.delwarp.message", prefix)
                        )
                )
        );
    }

    public record Config(
            Commands commands
    ) {
        public record Commands(
                Warp warp,
                SetWarp setwarp,
                DelWarp delwarp
        ) {
            public record Warp(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent noWarp,
                    TextComponent message
            ) {
            }

            public record SetWarp(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent message
            ) {
            }

            public record DelWarp(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent noWarp,
                    TextComponent message
            ) {
            }
        }
    }
}
