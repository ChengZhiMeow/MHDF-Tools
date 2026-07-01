package cn.chengzhimeow.mhdftools.bukkit.module.core.config;

import cn.chengzhimeow.mhdftools.bukkit.module.core.ModuleMain;
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
                super.component("command_info_format", prefix),
                new Config.Commands(
                        new Config.Commands.MHDFTools(
                                super.component("commands.mhdftools.usage", prefix),
                                super.component("commands.mhdftools.description", prefix),
                                new Config.Commands.MHDFTools.SubCommands(
                                        new Config.Commands.MHDFTools.SubCommands.Help(
                                                super.component("commands.mhdftools.sub_commands.help.usage", prefix),
                                                super.component("commands.mhdftools.sub_commands.help.description", prefix),
                                                super.component("commands.mhdftools.sub_commands.help.message", prefix)
                                        ),
                                        new Config.Commands.MHDFTools.SubCommands.Feature(
                                                super.component("commands.mhdftools.sub_commands.feature.usage", prefix),
                                                super.component("commands.mhdftools.sub_commands.feature.description", prefix),
                                                super.component("commands.mhdftools.sub_commands.feature.message", prefix)
                                        ),
                                        new Config.Commands.MHDFTools.SubCommands.Reload(
                                                super.component("commands.mhdftools.sub_commands.reload.usage", prefix),
                                                super.component("commands.mhdftools.sub_commands.reload.description", prefix),
                                                super.component("commands.mhdftools.sub_commands.reload.message", prefix)
                                        )
                                )
                        )
                )
        );
    }

    public record Config(
            TextComponent commandInfoFormat,
            Commands commands
    ) {
        public record Commands(
                MHDFTools mhdftools
        ) {
            public record MHDFTools(
                    TextComponent usage,
                    TextComponent description,
                    SubCommands subCommands
            ) {
                public record SubCommands(
                        Help help,
                        Feature feature,
                        Reload reload
                ) {
                    public record Help(
                            TextComponent usage,
                            TextComponent description,
                            TextComponent message
                    ) {
                    }

                    public record Feature(
                            TextComponent usage,
                            TextComponent description,
                            TextComponent message
                    ) {
                    }

                    public record Reload(
                            TextComponent usage,
                            TextComponent description,
                            TextComponent message
                    ) {
                    }
                }
            }
        }
    }
}
