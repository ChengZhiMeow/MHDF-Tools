package cn.chengzhiya.mhdftools.listener.misc;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.location.BungeeCordLocation;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.teleport.TeleportUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.concurrent.ConcurrentHashMap;

public final class TpLocation extends AbstractListener {
    public TpLocation() {
        super();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String tpLocationBase64 = Main.instance.getCacheManager().get("tpLocation", player.getName());
        if (tpLocationBase64 == null) {
            return;
        }

        Main.instance.getCacheManager().remove("tpLocation", player.getName());

        BungeeCordLocation tpLocation = new BungeeCordLocation(tpLocationBase64);
        if (!tpLocation.getServer().equals(Main.instance.getBungeeCordManager().getServerName())) {
            return;
        }

        TeleportUtil.teleport(player, tpLocation.toLocation(), new ConcurrentHashMap<>());
    }
}