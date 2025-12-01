package cn.chengzhimeow.mhdftools.bukkit.util.teleport;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public final class TeleportUtil {

    /**
     * 传送玩家
     *
     * @param player   玩家实例
     * @param location 位置实例
     * @param map      用于记录玩家传送尝试次数的映射表。
     */
    public static void teleport(Player player, Location location, ConcurrentHashMap<String, Integer> map) {
        if (location.getChunk().isLoaded()) location.getChunk().load(true);

        player.teleportAsync(location).thenAccept(success -> {
            location.setPitch(Math.max(-90f, Math.min(90f, location.getPitch())));

            int times = map.getOrDefault(player.getName(), 0);
            int maxTimes = ConfigSetting.getInstance().getData().getInt("bungeecord.autoTry.maxTimes");

            if (success) {
                if (player.getLocation().getWorld() != location.getWorld() || player.getLocation().distance(location) < 5.0) {
                    map.remove(player.getName());
                    return;
                }
            }

            if (times >= maxTimes) {
                map.remove(player.getName());
                return;
            }

            map.put(player.getName(), times + 1);
            int delay = ConfigSetting.getInstance().getData().getInt("bungeecord.autoTry.delay");
            CCScheduler.getInstance().getGlobalRegionScheduler().runTaskLater(Main.instance, () ->
                    TeleportUtil.teleport(player, location, map), delay);
        });
    }
}
