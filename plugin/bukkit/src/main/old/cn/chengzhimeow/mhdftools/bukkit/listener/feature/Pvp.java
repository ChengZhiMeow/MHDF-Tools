package cn.chengzhimeow.mhdftools.bukkit.listener.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public final class Pvp extends Listener {
    public Pvp() {
        super(
                List.of("pvpSettings.enable")
        );
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager) || !(event.getEntity() instanceof Player player)) return;

        MHDFToolsPlayer damagerPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(damager);
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
        if (damagerPlayer == null || mhdfPlayer == null) return;
        if (damagerPlayer.isEnablePvp() && mhdfPlayer.isEnablePvp()) return;

        event.setCancelled(true);
    }
}
