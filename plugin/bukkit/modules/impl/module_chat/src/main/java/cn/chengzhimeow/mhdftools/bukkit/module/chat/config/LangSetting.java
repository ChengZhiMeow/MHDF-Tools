package cn.chengzhimeow.mhdftools.bukkit.module.chat.config;

import cn.chengzhimeow.mhdftools.bukkit.module.chat.ModuleMain;
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
                new Config.Chat(
                        super.component("chat.delay", prefix),
                        super.component("chat.spam", prefix),
                        new Config.Chat.At(
                                super.component("chat.at.all", prefix),
                                super.component("chat.at.format", prefix),
                                new Config.Chat.At.AtTitle(
                                        super.component("chat.at.title.title", prefix),
                                        super.component("chat.at.title.subtitle", prefix),
                                        super.getData().getInt("chat.at.title.in"),
                                        super.getData().getInt("chat.at.title.step"),
                                        super.getData().getInt("chat.at.title.out")
                                )
                        )
                ),
                new Config.Commands(
                        new Config.Commands.Msg(
                                super.component("commands.msg.usage", prefix),
                                super.component("commands.msg.description", prefix),
                                super.component("commands.msg.receive", prefix),
                                super.component("commands.msg.send", prefix)
                        ),
                        new Config.Commands.Reply(
                                super.component("commands.reply.usage", prefix),
                                super.component("commands.reply.description", prefix),
                                super.component("commands.reply.no_target", prefix)
                        ),
                        new Config.Commands.Look(
                                super.component("commands.lookitem.usage", prefix),
                                super.component("commands.lookitem.description", prefix),
                                super.getData().getString("commands.lookitem.title", "展示物品"),
                                super.component("commands.lookitem.no_data", prefix),
                                super.component("commands.lookitem.message", prefix)
                        ),
                        new Config.Commands.Look(
                                super.component("commands.lookinventory.usage", prefix),
                                super.component("commands.lookinventory.description", prefix),
                                super.getData().getString("commands.lookinventory.title", "展示背包"),
                                super.component("commands.lookinventory.no_data", prefix),
                                super.component("commands.lookinventory.message", prefix)
                        ),
                        new Config.Commands.Look(
                                super.component("commands.lookenderchest.usage", prefix),
                                super.component("commands.lookenderchest.description", prefix),
                                super.getData().getString("commands.lookenderchest.title", "展示末影箱"),
                                super.component("commands.lookenderchest.no_data", prefix),
                                super.component("commands.lookenderchest.message", prefix)
                        )
                )
        );
    }

    public record Config(Chat chat, Commands commands) {
        public record Chat(TextComponent delay, TextComponent spam, At at) {
            public record At(TextComponent all, TextComponent format, AtTitle title) {
                public record AtTitle(TextComponent title, TextComponent subtitle, int in, int step, int out) {
                }
            }
        }

        public record Commands(Msg msg, Reply reply, Look lookItem, Look lookInventory, Look lookEnderChest) {
            public record Msg(TextComponent usage, TextComponent description, TextComponent receive,
                              TextComponent send) {
            }

            public record Reply(TextComponent usage, TextComponent description, TextComponent noTarget) {
            }

            public record Look(TextComponent usage, TextComponent description, String title, TextComponent noData,
                               TextComponent message) {
            }
        }
    }
}
