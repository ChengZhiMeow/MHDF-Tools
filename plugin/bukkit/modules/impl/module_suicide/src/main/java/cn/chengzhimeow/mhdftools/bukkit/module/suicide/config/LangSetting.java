package cn.chengzhimeow.mhdftools.bukkit.module.suicide.config;

import cn.chengzhimeow.mhdftools.bukkit.module.suicide.ModuleMain;
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
                        new Config.Commands.Suicide(
                                super.component("commands.suicide.usage", prefix),
                                super.component("commands.suicide.description", prefix),
                                super.component("commands.suicide.confirm", prefix),
                                super.component("commands.suicide.message", prefix)
                        )
                )
        );
    }

    public record Config(
            Commands commands
    ) {
        public record Commands(
                Suicide suicide
        ) {
            public record Suicide(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent confirm,
                    TextComponent message
            ) {
            }
        }
    }
}
