package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.Main;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;

public final class GameModeUtil {
    /**
     * 通过文本获取游戏模式实例
     *
     * @param gameModeString 游戏模式文本
     * @return 游戏模式实例
     */
    public static GameMode getGameMode(String gameModeString) {
        ConfigurationSection config = Main.instance.getConfigManager().getConfigManager().getData().getConfigurationSection("gamemodeSettings.gamemode");
        if (config == null) {
            return null;
        }

        if (config.getStringList("survival").contains(gameModeString)) return GameMode.SURVIVAL;
        if (config.getStringList("creative").contains(gameModeString)) return GameMode.CREATIVE;
        if (config.getStringList("spectator").contains(gameModeString)) return GameMode.SPECTATOR;
        if (config.getStringList("adventure").contains(gameModeString)) return GameMode.ADVENTURE;

        return null;
    }
}
