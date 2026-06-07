package cn.chengzhimeow.mhdftools.bukkit.module.gamemode.config;

import cn.chengzhimeow.mhdftools.bukkit.module.gamemode.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractLangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import lombok.Getter;
import org.bukkit.GameMode;

import java.util.EnumMap;
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

        Map<GameMode, TextComponent> gameModeNames = new EnumMap<>(GameMode.class);
        gameModeNames.put(GameMode.SURVIVAL, super.component("gamemode.SURVIVAL", prefix));
        gameModeNames.put(GameMode.ADVENTURE, super.component("gamemode.ADVENTURE", prefix));
        gameModeNames.put(GameMode.CREATIVE, super.component("gamemode.CREATIVE", prefix));
        gameModeNames.put(GameMode.SPECTATOR, super.component("gamemode.SPECTATOR", prefix));

        this.config = new Config(
                gameModeNames,
                new Config.Commands(
                        new Config.Commands.GameModeCommand(
                                super.component("commands.gamemode.usage", prefix),
                                super.component("commands.gamemode.description", prefix),
                                super.component("commands.gamemode.noGameMode", prefix),
                                super.component("commands.gamemode.message", prefix)
                        )
                )
        );
    }

    public record Config(
            Map<GameMode, TextComponent> gameMode,
            Commands commands
    ) {
        public TextComponent gameModeName(GameMode gameMode) {
            return this.gameMode.getOrDefault(gameMode, new TextComponent(gameMode.name()));
        }

        public record Commands(
                GameModeCommand gamemode
        ) {
            public record GameModeCommand(
                    TextComponent usage,
                    TextComponent description,
                    TextComponent noGameMode,
                    TextComponent message
            ) {
            }
        }
    }
}
