package cn.chengzhimeow.mhdftools.bukkit.module.tpa.config;

import cn.chengzhimeow.mhdftools.bukkit.module.tpa.ModuleMain;
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
                        new Config.Commands.Tpa(
                                super.component("commands.tpa.usage", prefix),
                                super.component("commands.tpa.description", prefix),
                                super.component("commands.tpa.message", prefix),
                                super.component("commands.tpa.request_message", prefix),
                                super.component("commands.tpa.in_delay", prefix),
                                super.component("commands.tpa.no_request", prefix),
                                super.component("commands.tpa.send_self", prefix),
                                new Config.Commands.Tpa.Accept(
                                        super.component("commands.tpa.accept.message", prefix),
                                        super.component("commands.tpa.accept.accepted", prefix)
                                ),
                                new Config.Commands.Tpa.Reject(
                                        super.component("commands.tpa.reject.message", prefix),
                                        super.component("commands.tpa.reject.rejected", prefix)
                                ),
                                new Config.Commands.Tpa.Delay(
                                        super.component("commands.tpa.delay.message", prefix),
                                        super.component("commands.tpa.delay.time_out", prefix)
                                )
                        )
                )
        );
    }

    public record Config(
            Commands commands
    ) {
        public record Commands(
                Tpa tpa
        ) {
            public record Tpa(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent message,
                    TextComponent requestMessage,
                    TextComponent inDelay,
                    TextComponent noRequest,
                    TextComponent sendSelf,
                    Accept accept,
                    Reject reject,
                    Delay delay
            ) {
                public record Accept(
                        TextComponent message,
                        TextComponent accepted
                ) {
                }

                public record Reject(
                        TextComponent message,
                        TextComponent rejected
                ) {
                }

                public record Delay(
                        TextComponent message,
                        TextComponent timeOut
                ) {
                }
            }
        }
    }
}
