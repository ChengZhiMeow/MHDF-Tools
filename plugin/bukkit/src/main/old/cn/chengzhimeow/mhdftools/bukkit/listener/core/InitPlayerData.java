package cn.chengzhimeow.mhdftools.bukkit.listener.core;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.database.data.PlayerData;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

final class InitPlayerData extends Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerData data = new PlayerData(event.getPlayer());
        MHDFToolsAPI.getInstance().getPlayerDataManager().update(data);
    }
}
