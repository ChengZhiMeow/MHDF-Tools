package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.feature.RandomTeleportUtil;
import cn.chengzhiya.mhdftools.util.world.BiomeUtil;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public final class RandomTeleport extends AbstractListener {
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
