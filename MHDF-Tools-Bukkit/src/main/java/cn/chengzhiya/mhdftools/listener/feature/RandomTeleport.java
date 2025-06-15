package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.RandomTeleportUtil;
import cn.chengzhiya.mhdftools.util.world.BiomeUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public final class RandomTeleport extends AbstractListener {
    public RandomTeleport() {
        super(
                "randomTeleportSettings.enable"
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String rtpWorld = Main.instance.getCacheManager().get("randomTeleportWorld", player.getName());
        if (rtpWorld == null) {
            return;
        }
        String rtpBiome = Main.instance.getCacheManager().get("randomTeleportBiome", player.getName());

        Main.instance.getCacheManager().remove("randomTeleportWorld", player.getName());

        World world = Bukkit.getWorld(rtpWorld);
        if (world == null) {
            ActionUtil.sendMessage(player, LangUtil.i18n("commands.randomteleport.subCommands.noWorld"));
        }

        Biome biome = null;
        if (rtpBiome != null) {
            biome = BiomeUtil.getBiome(rtpBiome);
        }

        RandomTeleportUtil.handleRandomTeleport(
                player,
                player,
                world,
                biome
        );
    }
}
