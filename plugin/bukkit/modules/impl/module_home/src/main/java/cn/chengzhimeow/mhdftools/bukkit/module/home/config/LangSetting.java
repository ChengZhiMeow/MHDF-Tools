package cn.chengzhimeow.mhdftools.bukkit.module.home.config;

import cn.chengzhimeow.mhdftools.bukkit.module.home.ModuleMain;
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
                        new Config.Commands.Home(
                                super.component("commands.home.usage", prefix),
                                super.component("commands.home.description", prefix),
                                super.component("commands.home.no_home", prefix),
                                super.component("commands.home.open_menu_message", prefix),
                                super.component("commands.home.invalid_name", prefix),
                                super.component("commands.home.message", prefix)
                        ),
                        new Config.Commands.SetHome(
                                super.component("commands.sethome.usage", prefix),
                                super.component("commands.sethome.description", prefix),
                                super.component("commands.sethome.have_home", prefix),
                                super.component("commands.sethome.is_max", prefix),
                                super.component("commands.sethome.message", prefix)
                        ),
                        new Config.Commands.DelHome(
                                super.component("commands.delhome.usage", prefix),
                                super.component("commands.delhome.description", prefix),
                                super.component("commands.delhome.no_home", prefix),
                                super.component("commands.delhome.message", prefix)
                        )
                )
        );
    }

    public record Config(
            Commands commands
    ) {
        public record Commands(
                Home home,
                SetHome sethome,
                DelHome delhome
        ) {
            public record Home(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent noHome,
                    TextComponent openMenuMessage,
                    TextComponent invalidName,
                    TextComponent message
            ) {
            }

            public record SetHome(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent haveHome,
                    TextComponent isMax,
                    TextComponent message
            ) {
            }

            public record DelHome(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent noHome,
                    TextComponent message
            ) {
            }
        }
    }
}
