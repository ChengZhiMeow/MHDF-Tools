package cn.chengzhimeow.mhdftools.bukkit.listener.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.listener.AbstractListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

final class TpaHere extends AbstractListener {
    public TpaHere() {
        super(
                List.of("tpahereSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Main.instance.getCacheManager().remove("tpaherePlayer", player.getName());
        Main.instance.getCacheManager().remove("tpahereDelay", player.getName());
    }
}
