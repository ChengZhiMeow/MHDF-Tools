package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

final class Back extends AbstractListener {
    public Back() {
        super(
                List.of("backSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!Main.instance.getConfigManager().getConfigManager().getData().getBoolean("backSettings.save.teleport")) {
            return;
        }

        MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(event.getPlayer());
        player.addBack("teleport", new BungeeCordLocation(event.getFrom()));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!Main.instance.getConfigManager().getConfigManager().getData().getBoolean("backSettings.save.death")) {
            return;
        }
        if (!Main.instance.getConfigManager().getConfigManager().getData().getBoolean("backSettings.respawnMessage")) {
            ActionUtil.sendMessage(event.getPlayer(), Main.instance.getConfigManager().getLangManager().i18n("commands.back.respawnMessage"));
        }

        MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(event.getPlayer());
        player.addBack("death", new BungeeCordLocation(event.getPlayer().getLocation()));
    }
}
