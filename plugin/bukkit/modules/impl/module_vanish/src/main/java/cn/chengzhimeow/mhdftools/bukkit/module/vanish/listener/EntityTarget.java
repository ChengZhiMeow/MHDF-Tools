package cn.chengzhimeow.mhdftools.bukkit.module.vanish.listener;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.config.ConfigSetting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTargetEvent;

final class EntityTarget extends Listener {
    public EntityTarget() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable()
        );
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        if (!ConfigSetting.getInstance().getConfig().cancelEntityTarget()) return;
        if (!(event.getTarget() instanceof Player player)) return;
        if (!MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId()).isEnableVanish()) return;

        event.setCancelled(true);
        event.setTarget(null);
    }
}
