package cn.chengzhimeow.mhdftools.bukkit.module.core.listener;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.database.data.PlayerData;
import cn.chengzhimeow.mhdftools.bukkit.module.core.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public final class InitPlayer extends Listener {
    public InitPlayer() {
        super(ModuleMain.instance);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerData data = MHDFToolsAPI.getInstance().getPlayerDataManager().getOrNull(player.getName());
        if (data != null) return;
        MHDFToolsAPI.getInstance().getPlayerDataManager().update(new PlayerData(player.getUniqueId(), player.getName()));
    }
}
