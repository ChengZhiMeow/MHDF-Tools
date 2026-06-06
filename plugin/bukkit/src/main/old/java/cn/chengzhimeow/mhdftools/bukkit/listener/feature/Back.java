package cn.chengzhimeow.mhdftools.bukkit.listener.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

final class Back extends Listener {
    public Back() {
        super(
                List.of("backSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!ConfigSetting.getInstance().getData().getBoolean("backSettings.save.teleport")) {
            return;
        }

        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(event.getPlayer());
        player.addBack("teleport", new BungeeCordLocation(event.getFrom()));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!ConfigSetting.getInstance().getData().getBoolean("backSettings.save.death")) {
            return;
        }
        if (!ConfigSetting.getInstance().getData().getBoolean("backSettings.respawnMessage")) {
            event.getPlayer().sendMessage(LangSetting.getInstance().i18n("commands.back.respawnMessage"));
        }

        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(event.getPlayer());
        player.addBack("death", new BungeeCordLocation(event.getPlayer().getLocation()));
    }
}
