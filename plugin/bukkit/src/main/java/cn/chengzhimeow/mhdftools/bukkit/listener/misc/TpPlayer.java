package cn.chengzhimeow.mhdftools.bukkit.listener.misc;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.listener.AbstractListener;
import cn.chengzhimeow.mhdftools.bukkit.util.teleport.TeleportUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.concurrent.ConcurrentHashMap;

final class TpPlayer extends AbstractListener {
    public TpPlayer() {
        super();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String targetPlayerName = Main.instance.getCacheManager().get("tpPlayer", player.getName());
        if (targetPlayerName == null) {
            return;
        }

        Main.instance.getCacheManager().remove("tpPlayer", player.getName());

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (targetPlayer == null) {
            return;
        }

        TeleportUtil.teleport(player, targetPlayer.getLocation(), new ConcurrentHashMap<>());
    }
}
