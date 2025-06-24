package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.location.BungeeCordLocation;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public final class Back extends AbstractListener {
    public Back() {
        super(
                List.of("backSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location location = player.getLocation();

        BungeeCordLocation bungeeCordLocation = new BungeeCordLocation(
                Main.instance.getBungeeCordManager().getServerName(),
                location
        );

        Main.instance.getCacheManager().put("back", player.getName(), bungeeCordLocation.toBase64());
    }
}
