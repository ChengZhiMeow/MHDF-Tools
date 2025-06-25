package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.entity.database.data.NickData;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.database.NickDataUtil;
import cn.chengzhiya.mhdftools.util.feature.NickUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public final class Nick extends AbstractListener {
    public Nick() {
        super(
                List.of("nickSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        NickData nickData = NickDataUtil.getNickData(player);
        if (nickData.getNick() == null) {
            return;
        }

        NickUtil.setNickDisplay(player, ColorUtil.color(nickData.getNick()), true);
    }
}
