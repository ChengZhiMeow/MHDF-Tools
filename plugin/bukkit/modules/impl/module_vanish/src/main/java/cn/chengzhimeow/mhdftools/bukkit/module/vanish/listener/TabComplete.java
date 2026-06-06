package cn.chengzhimeow.mhdftools.bukkit.module.vanish.listener;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.VanishStatus;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.config.ConfigSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.HashSet;
import java.util.Set;

final class TabComplete extends Listener {
    public TabComplete() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable()
        );
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTabComplete(TabCompleteEvent event) {
        if (!ConfigSetting.getInstance().getConfig().hideTabComplete()) return;

        Set<String> hiddenPlayerNames = new HashSet<>();
        for (VanishStatus vanishStatus : MHDFToolsAPI.getInstance().getVanishStatusManager().getList()) {
            if (!vanishStatus.isEnable()) continue;

            MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(vanishStatus.getPlayer());
            hiddenPlayerNames.add(player.getName());
        }
        if (hiddenPlayerNames.isEmpty()) return;

        event.getCompletions().removeIf(hiddenPlayerNames::contains);
    }
}
