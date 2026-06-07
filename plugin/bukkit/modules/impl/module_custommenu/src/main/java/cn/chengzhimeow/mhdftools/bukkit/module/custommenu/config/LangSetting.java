package cn.chengzhimeow.mhdftools.bukkit.module.custommenu.config;

import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.ModuleMain;
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
                        new Config.Commands.Custommenu(
                                super.component("commands.custommenu.usage", prefix),
                                super.component("commands.custommenu.description", prefix),
                                super.component("commands.custommenu.no_menu", prefix),
                                super.component("commands.custommenu.message", prefix)
                        )
                )
        );
    }

    public record Config(
            Commands commands
    ) {
        public record Commands(
                Custommenu custommenu
        ) {
            public record Custommenu(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent noMenu,
                    TextComponent message
            ) {
            }
        }
    }
}
