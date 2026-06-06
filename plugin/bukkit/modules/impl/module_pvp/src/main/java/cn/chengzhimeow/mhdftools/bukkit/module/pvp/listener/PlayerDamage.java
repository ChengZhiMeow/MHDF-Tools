package cn.chengzhimeow.mhdftools.bukkit.module.pvp.listener;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.module.pvp.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.pvp.config.ConfigSetting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

final class PlayerDamage extends Listener {
    public PlayerDamage() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable()
        );
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager)) return;
        if (!(event.getEntity() instanceof Player target)) return;

        MHDFToolsPlayer damagerPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(damager.getUniqueId());
        MHDFToolsPlayer targetPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(target.getUniqueId());
        if (damagerPlayer.isEnablePvp() && targetPlayer.isEnablePvp()) return;

        event.setCancelled(true);
    }
}
