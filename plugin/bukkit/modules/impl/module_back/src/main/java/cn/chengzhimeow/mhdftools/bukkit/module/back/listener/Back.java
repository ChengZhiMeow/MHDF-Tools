package cn.chengzhimeow.mhdftools.bukkit.module.back.listener;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkitAdapt;
import cn.chengzhimeow.mhdftools.bukkit.module.back.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.back.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.back.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class Back extends Listener {
    public Back() {
        super(ModuleMain.instance, ConfigSetting.getInstance().getConfig().enable());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!ConfigSetting.getInstance().getConfig().save().teleport()) return;

        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
        player.addBack("teleport", this.location(event.getFrom()));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!ConfigSetting.getInstance().getConfig().save().death()) return;

        if (ConfigSetting.getInstance().getConfig().respawnMessage()) {
            event.getPlayer().sendMessage(LangSetting.getInstance().getConfig().commands().back().respawnMessage());
        }

        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
        player.addBack("death", this.location(event.getPlayer().getLocation()));
    }

    private BungeeCordLocation location(Location location) {
        return new BungeeCordLocation(MHDFToolsBukkitAdapt.adapt(location));
    }
}
