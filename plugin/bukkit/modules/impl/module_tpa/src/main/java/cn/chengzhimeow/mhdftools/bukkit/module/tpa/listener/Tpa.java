package cn.chengzhimeow.mhdftools.bukkit.module.tpa.listener;

import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.ConfigSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public final class Tpa extends Listener {
    public Tpa() {
        super(ModuleMain.instance, ConfigSetting.getInstance().getConfig().enable());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ModuleMain.instance.getRequestCache().remove(event.getPlayer().getName());
    }
}
