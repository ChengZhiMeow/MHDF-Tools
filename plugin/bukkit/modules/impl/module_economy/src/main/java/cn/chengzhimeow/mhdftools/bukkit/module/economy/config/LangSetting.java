package cn.chengzhimeow.mhdftools.bukkit.module.economy.config;

import cn.chengzhimeow.mhdftools.bukkit.module.economy.ModuleMain;
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
                new Config.Economy(
                        super.component("economy.tax", prefix)
                ),
                new Config.Commands(
                        new Config.Commands.Money(
                                super.component("commands.money.usage", prefix),
                                super.component("commands.money.description", prefix),
                                super.component("commands.money.message", prefix)
                        ),
                        new Config.Commands.Pay(
                                super.component("commands.pay.usage", prefix),
                                super.component("commands.pay.description", prefix),
                                super.component("commands.pay.pay_self", prefix),
                                super.component("commands.pay.money_format_error", prefix),
                                super.component("commands.pay.no_money", prefix),
                                super.component("commands.pay.message", prefix),
                                super.component("commands.pay.received_message", prefix)
                        ),
                        new Config.Commands.MoneyAdmin(
                                super.component("commands.moneyadmin.usage", prefix),
                                super.component("commands.moneyadmin.description", prefix),
                                super.component("commands.moneyadmin.command_info_format", prefix),
                                new Config.Commands.MoneyAdmin.SubCommands(
                                        new Config.Commands.MoneyAdmin.SubCommands.Help(
                                                super.component("commands.moneyadmin.sub_commands.help.usage", prefix),
                                                super.component("commands.moneyadmin.sub_commands.help.description", prefix),
                                                super.component("commands.moneyadmin.sub_commands.help.message", prefix)
                                        ),
                                        new Config.Commands.MoneyAdmin.SubCommands.Set(
                                                super.component("commands.moneyadmin.sub_commands.set.usage", prefix),
                                                super.component("commands.moneyadmin.sub_commands.set.description", prefix),
                                                super.component("commands.moneyadmin.sub_commands.set.message", prefix)
                                        ),
                                        new Config.Commands.MoneyAdmin.SubCommands.Add(
                                                super.component("commands.moneyadmin.sub_commands.add.usage", prefix),
                                                super.component("commands.moneyadmin.sub_commands.add.description", prefix),
                                                super.component("commands.moneyadmin.sub_commands.add.message", prefix)
                                        ),
                                        new Config.Commands.MoneyAdmin.SubCommands.Take(
                                                super.component("commands.moneyadmin.sub_commands.take.usage", prefix),
                                                super.component("commands.moneyadmin.sub_commands.take.description", prefix),
                                                super.component("commands.moneyadmin.sub_commands.take.message", prefix)
                                        )
                                )
                        )
                )
        );
    }

    public record Config(
            TextComponent commandInfoFormat,
            Economy economy,
            Commands commands
    ) {
        public record Economy(
                TextComponent tax
        ) {
        }

        public record Commands(
                Money money,
                Pay pay,
                MoneyAdmin moneyadmin
        ) {
            public record Money(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent message
            ) {
            }

            public record Pay(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent paySelf,
                    TextComponent moneyFormatError,
                    TextComponent noMoney,
                    TextComponent message,
                    TextComponent receivedMessage
            ) {
            }

            public record MoneyAdmin(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent moneyFormatError,
                    SubCommands subCommands
            ) {
                public record SubCommands(
                        Help help,
                        Set set,
                        Add add,
                        Take take
                ) {
                    public record Help(
                            TextComponent usage,
                            TextComponent description,
                            TextComponent message
                    ) {
                    }

                    public record Set(
                            TextComponent usage,
                            TextComponent description,
                            TextComponent message
                    ) {
                    }

                    public record Add(
                            TextComponent usage,
                            TextComponent description,
                            TextComponent message
                    ) {
                    }

                    public record Take(
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
