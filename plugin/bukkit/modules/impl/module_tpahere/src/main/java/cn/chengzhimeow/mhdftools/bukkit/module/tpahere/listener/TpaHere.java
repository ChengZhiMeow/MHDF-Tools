package cn.chengzhimeow.mhdftools.bukkit.module.tpahere.listener;

import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.tpahere.config.ConfigSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

final class TpaHere extends Listener {
    public TpaHere() {
        super(ModuleMain.instance, ConfigSetting.getInstance().getConfig().enable());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ModuleMain.instance.getRequestCache().remove(event.getPlayer().getName());
    }
}
