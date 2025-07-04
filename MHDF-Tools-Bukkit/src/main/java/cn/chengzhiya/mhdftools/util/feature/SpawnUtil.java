package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public final class SpawnUtil {
    /**
     * 传送指定玩家实例至出生点
     *
     * @param player 玩家实例
     */
    public static void teleportSpawn(Player player) {
        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("spawnSettings.location");
        if (config == null) {
            return;
        }

        BungeeCordLocation bungeeCordLocation = new BungeeCordLocation(
                config.getString("server"),
                config.getString("world"),
                config.getDouble("x"),
                config.getDouble("y"),
                config.getDouble("z"),
                (float) config.getDouble("yaw"),
                (float) config.getDouble("pitch")
        );

        Main.instance.getBungeeCordManager().teleportLocation(player, bungeeCordLocation);
    }
}
