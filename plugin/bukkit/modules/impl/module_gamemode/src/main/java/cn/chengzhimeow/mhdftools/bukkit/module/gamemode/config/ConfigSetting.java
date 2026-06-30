package cn.chengzhimeow.mhdftools.bukkit.module.gamemode.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.module.gamemode.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;
import org.bukkit.GameMode;

import java.util.*;

public final class ConfigSetting extends AbstractYamlSetting<ConfigSetting.Config> {
    @Getter(lazy = true)
    private static final ConfigSetting instance = new ConfigSetting();
    @Getter private Config config;

    private ConfigSetting() {
    }

    @Override
    public String originFilePath() {
        return "module/" + ModuleMain.instance.getId() + "/config.yml";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }

    @Override
    public void reload() {
        super.reload();

        Map<String, GameMode> aliases = new LinkedHashMap<>();
        ConfigurationSection gameModeSection = super.getData().getConfigurationSection("gamemode");
        if (gameModeSection != null) {
            gameModeSection.getStringList("survival").forEach(alias -> aliases.put(alias.toLowerCase(Locale.ROOT), GameMode.SURVIVAL));
            gameModeSection.getStringList("creative").forEach(alias -> aliases.put(alias.toLowerCase(Locale.ROOT), GameMode.CREATIVE));
            gameModeSection.getStringList("adventure").forEach(alias -> aliases.put(alias.toLowerCase(Locale.ROOT), GameMode.ADVENTURE));
            gameModeSection.getStringList("spectator").forEach(alias -> aliases.put(alias.toLowerCase(Locale.ROOT), GameMode.SPECTATOR));
        }

        this.config = new Config(
                super.getData().getBoolean("enable"),
                new EnumMap<>(Map.of(
                        GameMode.SURVIVAL, List.copyOf(gameModeSection == null ? List.of() : gameModeSection.getStringList("survival")),
                        GameMode.CREATIVE, List.copyOf(gameModeSection == null ? List.of() : gameModeSection.getStringList("creative")),
                        GameMode.ADVENTURE, List.copyOf(gameModeSection == null ? List.of() : gameModeSection.getStringList("adventure")),
                        GameMode.SPECTATOR, List.copyOf(gameModeSection == null ? List.of() : gameModeSection.getStringList("spectator"))
                )),
                aliases,
                super.getData().getStringList("tabCompleter"),
                super.getData().getStringList("commands")
        );
    }

    public record Config(
            boolean enable,
            Map<GameMode, List<String>> gameMode,
            Map<String, GameMode> aliases,
            List<String> tabCompleter,
            List<String> commands
    ) {
        public GameMode getGameMode(String value) {
            return this.aliases.get(value.toLowerCase(Locale.ROOT));
        }

        public List<String> tabCompleter() {
            return new ArrayList<>(this.tabCompleter);
        }
    }
}
