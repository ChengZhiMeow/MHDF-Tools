package cn.chengzhimeow.mhdftools.bukkit.listener.core;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.database.data.PlayerData;
import cn.chengzhimeow.mhdftools.bukkit.listener.AbstractListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

final class InitPlayerData extends AbstractListener {
    public InitPlayerData() {
        super();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerData data = new PlayerData(event.getPlayer());
        MHDFToolsAPIHelper.getInstance().getPlayerDataManager().update(data);
    }
}
