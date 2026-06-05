package cn.chengzhimeow.mhdftools.bukkit.module.hat.config;

import cn.chengzhimeow.mhdftools.bukkit.module.hat.ModuleMain;
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
                        new Config.Commands.Hat(
                                super.component("commands.hat.usage", prefix),
                                super.component("commands.hat.description", prefix),
                                super.component("commands.hat.no_item", prefix),
                                super.component("commands.hat.message", prefix)
                        )
                )
        );
    }

    public record Config(
            Commands commands
    ) {
        public record Commands(
                Hat hat
        ) {
            public record Hat(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent noItem,
                    TextComponent message
            ) {
            }
        }
    }
}
