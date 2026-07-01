package cn.chengzhimeow.mhdftools.bukkit.module.fly.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.module.fly.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractLangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

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
                        new Config.Commands.Fly(
                                super.component("commands.fly.usage", prefix),
                                super.component("commands.fly.description", prefix),
                                super.component("commands.fly.message", prefix),
                                this.fallMessages(prefix)
                        ),
                        new Config.Commands.FlyTime(
                                super.component("commands.flytime.usage", prefix),
                                super.component("commands.flytime.description", prefix),
                                super.component("commands.flytime.time_format_error", prefix),
                                new Config.Commands.FlyTime.SubCommands(
                                        new Config.Commands.FlyTime.SubCommands.Help(
                                                super.component("commands.flytime.sub_commands.help.usage", prefix),
                                                super.component("commands.flytime.sub_commands.help.description", prefix),
                                                super.component("commands.flytime.sub_commands.help.message", prefix)
                                        ),
                                        new Config.Commands.FlyTime.SubCommands.Set(
                                                super.component("commands.flytime.sub_commands.set.usage", prefix),
                                                super.component("commands.flytime.sub_commands.set.description", prefix),
                                                super.component("commands.flytime.sub_commands.set.message", prefix)
                                        ),
                                        new Config.Commands.FlyTime.SubCommands.Add(
                                                super.component("commands.flytime.sub_commands.add.usage", prefix),
                                                super.component("commands.flytime.sub_commands.add.description", prefix),
                                                super.component("commands.flytime.sub_commands.add.message", prefix)
                                        ),
                                        new Config.Commands.FlyTime.SubCommands.Take(
                                                super.component("commands.flytime.sub_commands.take.usage", prefix),
                                                super.component("commands.flytime.sub_commands.take.description", prefix),
                                                super.component("commands.flytime.sub_commands.take.message", prefix)
                                        )
                                )
                        )
                )
        );
    }

    private Map<Long, Config.Commands.Fly.FallMessage> fallMessages(TextComponent prefix) {
        Map<Long, Config.Commands.Fly.FallMessage> messages = new HashMap<>();
        ConfigurationSection section = super.getData().getConfigurationSection("commands.fly.fall_messages");
        if (section == null) return messages;

        for (String key : section.getKeys(false)) {
            try {
                messages.put(
                        Long.parseLong(key),
                        new Config.Commands.Fly.FallMessage(
                                super.component("commands.fly.fall_messages." + key + ".title", prefix),
                                super.component("commands.fly.fall_messages." + key + ".subtitle", prefix),
                                section.getInt(key + ".in"),
                                section.getInt(key + ".step"),
                                section.getInt(key + ".out")
                        )
                );
            } catch (NumberFormatException ignored) {
            }
        }
        return messages;
    }

    public record Config(
            Commands commands
    ) {
        public record Commands(
                Fly fly,
                FlyTime flytime
        ) {
            public record Fly(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent message,
                    Map<Long, FallMessage> fallMessages
            ) {
                public record FallMessage(
                        TextComponent title,
                        TextComponent subtitle,
                        int in,
                        int step,
                        int out
                ) {
                }
            }

            public record FlyTime(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent timeFormatError,
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
