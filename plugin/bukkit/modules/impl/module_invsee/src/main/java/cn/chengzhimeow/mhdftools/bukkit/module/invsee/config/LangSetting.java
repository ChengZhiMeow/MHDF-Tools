package cn.chengzhimeow.mhdftools.bukkit.module.invsee.config;

import cn.chengzhimeow.mhdftools.bukkit.module.invsee.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractLangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import lombok.Getter;

public final class LangSetting extends AbstractLangSetting<LangSetting.Config> {
    @Getter(lazy = true)
    private static final LangSetting instance = new LangSetting();
    @Getter private Config config;

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
                        new Config.Commands.Invsee(
                                super.component("commands.invsee.usage", prefix),
                                super.component("commands.invsee.description", prefix),
                                new Config.Commands.Invsee.Types(
                                        super.component("commands.invsee.types.armor", prefix),
                                        super.component("commands.invsee.types.inventory", prefix),
                                        super.component("commands.invsee.types.enderchest", prefix)
                                ),
                                super.component("commands.invsee.no_type", prefix),
                                super.component("commands.invsee.message", prefix)
                        )
                )
        );
    }

    public record Config(
            Commands commands
    ) {
        public record Commands(
                Invsee invsee
        ) {
            public record Invsee(
                    TextComponent usage,
                    TextComponent description,
                    Types types,
                    TextComponent noType,
                    TextComponent message
            ) {
                public record Types(
                        TextComponent armor,
                        TextComponent inventory,
                        TextComponent enderchest
                ) {
                    public TextComponent get(String type) {
                        return switch (type) {
                            case "armor" -> this.armor;
                            case "inventory" -> this.inventory;
                            case "enderchest" -> this.enderchest;
                            default -> new TextComponent(type);
                        };
                    }
                }
            }
        }
    }
}
