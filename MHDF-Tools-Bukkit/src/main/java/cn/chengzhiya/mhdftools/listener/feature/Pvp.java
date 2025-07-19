package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public final class Pvp extends AbstractListener {
    public Pvp() {
        super(
                List.of("pvpSettings.enable")
        );
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager) || !(event.getEntity() instanceof Player player)) {
            return;
        }

        MHDFToolsPlayer damagerPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(damager);
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        if (damagerPlayer == null || mhdfPlayer == null) {
            return;
        }

        if (damagerPlayer.isEnablePvp() && mhdfPlayer.isEnablePvp()) {
            return;
        }

        event.setCancelled(true);
    }
}
