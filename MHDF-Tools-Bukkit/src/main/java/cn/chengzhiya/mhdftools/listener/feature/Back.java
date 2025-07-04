package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

public final class Back extends AbstractListener {
    public Back() {
        super(
                List.of("backSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location location = player.getLocation();

        BungeeCordLocation bungeeCordLocation = new BungeeCordLocation(
                Main.instance.getBungeeCordManager().getServerName(),
                location
        );

        Main.instance.getCacheManager().put("back", player.getName(), bungeeCordLocation.toString());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!ConfigUtil.getConfig().getBoolean("backSettings.respawnMessage")) {
            return;
        }

        Player player = event.getPlayer();
        ActionUtil.sendMessage(player, LangUtil.i18n("commands.back.respawnMessage"));
    }
}
