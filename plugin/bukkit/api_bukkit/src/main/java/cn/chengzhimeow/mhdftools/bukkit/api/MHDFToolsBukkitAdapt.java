package cn.chengzhimeow.mhdftools.bukkit.api;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.location.BukkitLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class MHDFToolsBukkitAdapt {
    public static MHDFToolsPlayer adapt(Player player) {
        return MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
    }

    public static BukkitLocation adapt(Location location) {
        return new BukkitLocation(
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }
}
