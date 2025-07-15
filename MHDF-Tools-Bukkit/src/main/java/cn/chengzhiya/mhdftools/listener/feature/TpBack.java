package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

final class TpBack extends AbstractListener {
    public TpBack() {
        super(
                List.of("tpbackSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location location = event.getFrom();

        BungeeCordLocation bungeeCordLocation = new BungeeCordLocation(
                Main.instance.getBungeeCordManager().getServerName(),
                location
        );

        Main.instance.getCacheManager().put("tpback", player.getName(), bungeeCordLocation.toString());
    }
}
