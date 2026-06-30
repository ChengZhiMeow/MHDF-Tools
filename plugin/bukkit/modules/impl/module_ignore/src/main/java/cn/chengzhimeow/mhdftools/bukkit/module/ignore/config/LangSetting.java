package cn.chengzhimeow.mhdftools.bukkit.module.ignore.config;

import cn.chengzhimeow.mhdftools.bukkit.module.ignore.ModuleMain;
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
                        new Config.Commands.Ignore(
                                super.component("commands.ignore.usage", prefix),
                                super.component("commands.ignore.description", prefix),
                                new Config.Commands.Ignore.SubCommands(
                                        new Config.Commands.Ignore.SubCommands.Help(
                                                super.component("commands.ignore.sub_commands.help.usage", prefix),
                                                super.component("commands.ignore.sub_commands.help.description", prefix),
                                                super.component("commands.ignore.sub_commands.help.message", prefix)
                                        ),
                                        new Config.Commands.Ignore.SubCommands.List(
                                                super.component("commands.ignore.sub_commands.list.usage", prefix),
                                                super.component("commands.ignore.sub_commands.list.description", prefix),
                                                super.component("commands.ignore.sub_commands.list.message", prefix)
                                        ),
                                        new Config.Commands.Ignore.SubCommands.Add(
                                                super.component("commands.ignore.sub_commands.add.usage", prefix),
                                                super.component("commands.ignore.sub_commands.add.description", prefix),
                                                super.component("commands.ignore.sub_commands.add.have_ignore", prefix),
                                                super.component("commands.ignore.sub_commands.add.blacklist", prefix),
                                                super.component("commands.ignore.sub_commands.add.ignore_self", prefix),
                                                super.component("commands.ignore.sub_commands.add.message", prefix)
                                        ),
                                        new Config.Commands.Ignore.SubCommands.Remove(
                                                super.component("commands.ignore.sub_commands.remove.usage", prefix),
                                                super.component("commands.ignore.sub_commands.remove.description", prefix),
                                                super.component("commands.ignore.sub_commands.remove.no_ignore", prefix),
                                                super.component("commands.ignore.sub_commands.remove.message", prefix)
                                        )
                                )
                        )
                )
        );
    }

    public record Config(
            Commands commands
    ) {
        public record Commands(
                Ignore ignore
        ) {
            public record Ignore(
                    TextComponent usage,
                    TextComponent description,
                    SubCommands subCommands
            ) {
                public record SubCommands(
                        Help help,
                        List list,
                        Add add,
                        Remove remove
                ) {
                    public record Help(
                            TextComponent usage,
                            TextComponent description,
                            TextComponent message
                    ) {
                    }

                    public record List(
                            TextComponent usage,
                            TextComponent description,
                            TextComponent message
                    ) {
                    }

                    public record Add(
                            TextComponent usage,
                            TextComponent description,
                            TextComponent haveIgnore,
                            TextComponent blacklist,
                            TextComponent ignoreSelf,
                            TextComponent message
                    ) {
                    }

                    public record Remove(
                            TextComponent usage,
                            TextComponent description,
                            TextComponent noIgnore,
                            TextComponent message
                    ) {
                    }
                }
            }
        }
    }
}
