package cn.chengzhimeow.mhdftools.bukkit.listener.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

final class Nick extends Listener {
    public Nick() {
        super(
                List.of("nickSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
        if (!mhdfPlayer.hasNickData()) {
            return;
        }

        mhdfPlayer.showNickDisplay();
    }
}
