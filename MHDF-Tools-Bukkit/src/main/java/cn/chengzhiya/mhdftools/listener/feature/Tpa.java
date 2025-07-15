package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

final class Tpa extends AbstractListener {
    public Tpa() {
        super(
                List.of("tpaSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Main.instance.getCacheManager().remove("tpaPlayer", player.getName());
        Main.instance.getCacheManager().remove("tpaDelay", player.getName());
    }
}
