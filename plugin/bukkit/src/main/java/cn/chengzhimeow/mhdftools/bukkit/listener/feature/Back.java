package cn.chengzhimeow.mhdftools.bukkit.listener.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.listener.AbstractListener;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

final class Back extends AbstractListener {
    public Back() {
        super(
                List.of("backSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!ConfigSetting.getSettingInstance().getData().getBoolean("backSettings.save.teleport")) {
            return;
        }

        MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(event.getPlayer());
        player.addBack("teleport", new BungeeCordLocation(event.getFrom()));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!ConfigSetting.getSettingInstance().getData().getBoolean("backSettings.save.death")) {
            return;
        }
        if (!ConfigSetting.getSettingInstance().getData().getBoolean("backSettings.respawnMessage")) {
            ActionUtil.sendMessage(event.getPlayer(), LangSetting.getSettingInstance().i18n("commands.back.respawnMessage"));
        }

        MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(event.getPlayer());
        player.addBack("death", new BungeeCordLocation(event.getPlayer().getLocation()));
    }
}
