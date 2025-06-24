package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.database.EconomyDataUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public final class Economy extends AbstractListener {
    public Economy() {
        super(
                List.of("economySettings.enable")
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        EconomyDataUtil.initEconomyData(player);
    }
}
