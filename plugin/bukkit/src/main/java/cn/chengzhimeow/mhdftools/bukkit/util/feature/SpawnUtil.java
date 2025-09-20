package cn.chengzhimeow.mhdftools.bukkit.util.feature;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import org.bukkit.entity.Player;

public final class SpawnUtil {
    /**
     * 传送指定玩家实例至出生点
     *
     * @param player 玩家实例
     */
    public static void teleportSpawn(Player player) {
        ConfigurationSection config = ConfigSetting.getSettingInstance().getData().getConfigurationSection("spawnSettings.location");
        if (config == null) return;

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
