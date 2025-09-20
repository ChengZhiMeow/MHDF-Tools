package cn.chengzhimeow.mhdftools.bukkit.listener.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.listener.AbstractListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

final class Nick extends AbstractListener {
    public Nick() {
        super(
                List.of("nickSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        if (!mhdfPlayer.hasNickData()) {
            return;
        }

        mhdfPlayer.showNickDisplay();
    }
}
