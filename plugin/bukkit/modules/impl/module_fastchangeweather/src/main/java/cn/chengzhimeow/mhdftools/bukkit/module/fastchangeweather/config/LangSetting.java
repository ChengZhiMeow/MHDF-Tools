package cn.chengzhimeow.mhdftools.bukkit.module.fastchangeweather.config;

import cn.chengzhimeow.mhdftools.bukkit.module.fastchangeweather.ModuleMain;
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
                        new Config.Commands.FastChangeWeather(
                                super.component("commands.fastchangeweather.usage", prefix),
                                super.component("commands.fastchangeweather.description", prefix),
                                super.component("commands.fastchangeweather.message", prefix)
                        )
                )
        );
    }

    public record Config(
            Commands commands
    ) {
        public record Commands(
                FastChangeWeather fastChangeWeather
        ) {
            public record FastChangeWeather(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent message
            ) {
            }
        }
    }
}
