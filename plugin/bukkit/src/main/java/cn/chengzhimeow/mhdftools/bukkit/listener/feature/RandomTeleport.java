package cn.chengzhimeow.mhdftools.bukkit.listener.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.listener.AbstractListener;
import cn.chengzhimeow.mhdftools.bukkit.reflection.world.BiomeUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.RandomTeleportUtil;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

final class RandomTeleport extends AbstractListener {
    public RandomTeleport() {
        super(
                List.of("randomTeleportSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String rtpWorld = Main.instance.getCacheManager().get("randomTeleportWorld", player.getName());
        if (rtpWorld == null) {
            return;
        }

        Biome biome = null;
        String rtpBiome = Main.instance.getCacheManager().get("randomTeleportBiome", player.getName());
        if (rtpBiome != null) {
            biome = BiomeUtil.getBiome(rtpBiome);
        }

        Main.instance.getCacheManager().remove("randomTeleportWorld", player.getName());
        Main.instance.getCacheManager().remove("randomTeleportBiome", player.getName());

        RandomTeleportUtil.handleRandomTeleport(
                player,
                player,
                rtpWorld,
                biome
        );
    }
}
