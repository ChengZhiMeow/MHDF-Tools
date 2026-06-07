package cn.chengzhimeow.mhdftools.bukkit.module.nick.listener;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.module.nick.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.nick.config.ConfigSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

final class PlayerJoin extends Listener {
    public PlayerJoin() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable()
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        if (!player.hasNickData()) return;

        player.showNickDisplay();
    }
}
