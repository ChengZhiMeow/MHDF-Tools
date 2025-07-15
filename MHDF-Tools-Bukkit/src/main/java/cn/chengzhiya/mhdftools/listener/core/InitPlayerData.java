package cn.chengzhiya.mhdftools.listener.core;

import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.database.data.PlayerData;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
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
