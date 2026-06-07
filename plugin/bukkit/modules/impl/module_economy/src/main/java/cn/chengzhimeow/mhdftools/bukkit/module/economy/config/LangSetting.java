package cn.chengzhimeow.mhdftools.bukkit.module.economy.config;

import cn.chengzhimeow.mhdftools.bukkit.module.economy.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractLangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

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
        List<String> moneyadminSubCommandNames = List.of("help", "set", "add", "take");

        this.config = new Config(
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
                                super.component("commands.pay.paySelf", prefix),
                                super.component("commands.pay.moneyFormatError", prefix),
                                super.component("commands.pay.noMoney", prefix),
                                super.component("commands.pay.message", prefix),
                                super.component("commands.pay.receivedMessage", prefix)
                        ),
                        new Config.Commands.MoneyAdmin(
                                super.component("commands.moneyadmin.usage", prefix),
                                super.component("commands.moneyadmin.description", prefix),
                                super.component("commands.moneyadmin.moneyFormatError", prefix),
                                moneyadminSubCommandNames,
                                this.subCommandHelpList("commands.moneyadmin.subCommands", moneyadminSubCommandNames),
                                new Config.Commands.MoneyAdmin.SubCommands(
                                        new Config.Commands.MoneyAdmin.SubCommands.Help(
                                                super.component("commands.moneyadmin.subCommands.help.usage", prefix),
                                                super.component("commands.moneyadmin.subCommands.help.description", prefix),
                                                super.component("commands.moneyadmin.subCommands.help.message", prefix)
                                        ),
                                        new Config.Commands.MoneyAdmin.SubCommands.Set(
                                                super.component("commands.moneyadmin.subCommands.set.usage", prefix),
                                                super.component("commands.moneyadmin.subCommands.set.description", prefix),
                                                super.component("commands.moneyadmin.subCommands.set.message", prefix)
                                        ),
                                        new Config.Commands.MoneyAdmin.SubCommands.Add(
                                                super.component("commands.moneyadmin.subCommands.add.usage", prefix),
                                                super.component("commands.moneyadmin.subCommands.add.description", prefix),
                                                super.component("commands.moneyadmin.subCommands.add.message", prefix)
                                        ),
                                        new Config.Commands.MoneyAdmin.SubCommands.Take(
                                                super.component("commands.moneyadmin.subCommands.take.usage", prefix),
                                                super.component("commands.moneyadmin.subCommands.take.description", prefix),
                                                super.component("commands.moneyadmin.subCommands.take.message", prefix)
                                        )
                                )
                        )
                )
        );
    }

    private TextComponent subCommandHelpList(String path, List<String> names) {
        String format = super.getData().getString("commandInfoFormat", "  &f{usage}  \n &8 - &7{description}");
        StringBuilder builder = new StringBuilder();
        for (String name : names) {
            if (!builder.isEmpty()) builder.append("\n");
            builder.append(Objects.requireNonNull(format)
                    .replace("{usage}", Objects.requireNonNull(super.getData().getString(path + "." + name + ".usage")))
                    .replace("{description}", Objects.requireNonNull(super.getData().getString(path + "." + name + ".description"))));
        }
        return ColorUtil.color(builder.toString());
    }

    public record Config(
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
                    List<String> subCommandNames,
                    TextComponent helpList,
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
