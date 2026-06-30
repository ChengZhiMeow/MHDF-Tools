package cn.chengzhimeow.mhdftools.bukkit.module.back.config;

import cn.chengzhimeow.mhdftools.bukkit.module.back.ModuleMain;
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
                        new Config.Commands.Back(
                                super.component("commands.back.usage", prefix),
                                super.component("commands.back.description", prefix),
                                super.component("commands.back.no_location", prefix),
                                super.component("commands.back.respawn_message", prefix),
                                super.component("commands.back.open_menu_message", prefix),
                                super.component("commands.back.message", prefix),
                                new Config.Commands.Back.Types(
                                        super.getData().getString("commands.back.types.teleport", "传送前的位置"),
                                        super.getData().getString("commands.back.types.death", "死亡位置")
                                )
                        )
                )
        );
    }

    public record Config(
            Commands commands
    ) {
        public record Commands(
                Back back
        ) {
            public record Back(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent noLocation,
                    TextComponent respawnMessage,
                    TextComponent openMenuMessage,
                    TextComponent message,
                    Types types
            ) {
                public String typeName(String type) {
                    return switch (type) {
                        case "death" -> this.types.death();
                        case "teleport" -> this.types.teleport();
                        default -> type;
                    };
                }

                public record Types(
                        String teleport,
                        String death
                ) {
                }
            }
        }
    }
}
