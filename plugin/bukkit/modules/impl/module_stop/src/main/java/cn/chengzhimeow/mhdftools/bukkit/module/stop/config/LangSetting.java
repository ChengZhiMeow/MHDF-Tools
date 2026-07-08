package cn.chengzhimeow.mhdftools.bukkit.module.stop.config;

import cn.chengzhimeow.mhdftools.bukkit.module.stop.ModuleMain;
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
                        new Config.Commands.Stop(
                                super.component("commands.stop.usage", prefix),
                                super.component("commands.stop.description", prefix),
                                super.component("commands.stop.default_reason", prefix),
                                super.component("commands.stop.time_format_error", prefix),
                                super.component("commands.stop.countdown", prefix),
                                super.component("commands.stop.kick_message", prefix),
                                new Config.Commands.Stop.SubCommands(
                                        new Config.Commands.Stop.SubCommands.Help(
                                                super.component("commands.stop.sub_commands.help.usage", prefix),
                                                super.component("commands.stop.sub_commands.help.description", prefix),
                                                super.component("commands.stop.sub_commands.help.message", prefix)
                                        ),
                                        new Config.Commands.Stop.SubCommands.Cancel(
                                                super.component("commands.stop.sub_commands.cancel.usage", prefix),
                                                super.component("commands.stop.sub_commands.cancel.description", prefix),
                                                super.component("commands.stop.sub_commands.cancel.no_stop", prefix),
                                                super.component("commands.stop.sub_commands.cancel.message", prefix)
                                        ),
                                        new Config.Commands.Stop.SubCommands.Confirm(
                                                super.component("commands.stop.sub_commands.confirm.usage", prefix),
                                                super.component("commands.stop.sub_commands.confirm.description", prefix),
                                                super.component("commands.stop.sub_commands.confirm.no_stop", prefix),
                                                super.component("commands.stop.sub_commands.confirm.message", prefix)
                                        ),
                                        new Config.Commands.Stop.SubCommands.Default(
                                                super.component("commands.stop.sub_commands.default.usage", prefix),
                                                super.component("commands.stop.sub_commands.default.description", prefix),
                                                super.component("commands.stop.sub_commands.default.in_stop", prefix),
                                                super.component("commands.stop.sub_commands.default.message", prefix)
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
                Stop stop
        ) {
            public record Stop(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent defaultReason,
                    TextComponent timeFormatError,
                    TextComponent countdown,
                    TextComponent kickMessage,
                    SubCommands subCommands
            ) {
                public record SubCommands(
                        Help help,
                        Cancel cancel,
                        Confirm confirm,
                        Default defaultCommand
                ) {
                    public record Help(
                            TextComponent usage,
                            TextComponent description,
                            TextComponent message
                    ) {
                    }

                    public record Cancel(
                            TextComponent usage,
                            TextComponent description,
                            TextComponent noStop,
                            TextComponent message
                    ) {
                    }

                    public record Confirm(
                            TextComponent usage,
                            TextComponent description,
                            TextComponent noStop,
                            TextComponent message
                    ) {
                    }

                    public record Default(
                            TextComponent usage,
                            TextComponent description,
                            TextComponent inStop,
                            TextComponent message
                    ) {
                    }
                }
            }
        }
    }
}
