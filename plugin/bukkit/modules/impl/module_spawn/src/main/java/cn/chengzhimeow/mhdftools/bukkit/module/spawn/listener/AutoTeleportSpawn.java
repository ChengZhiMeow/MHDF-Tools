package cn.chengzhimeow.mhdftools.bukkit.module.spawn.listener;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.module.spawn.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.spawn.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.spawn.config.SpawnSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

final class AutoTeleportSpawn extends Listener {
    public AutoTeleportSpawn() {
        super(ModuleMain.instance, ConfigSetting.getInstance().getConfig().enable());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!ConfigSetting.getInstance().getConfig().autoTeleport().join()) return;
        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
        player.teleport(SpawnSetting.getInstance().getConfig().location());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!ConfigSetting.getInstance().getConfig().autoTeleport().respawn()) return;
        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
        player.teleport(SpawnSetting.getInstance().getConfig().location());
    }
}
