package cn.chengzhimeow.mhdftools.bukkit.listener.feature;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.SpawnUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

final class AutoTeleportSpawn extends Listener {
    public AutoTeleportSpawn() {
        super(
                List.of("spawnSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ConfigurationSection config = ConfigSetting.getInstance().getData().getConfigurationSection("spawnSettings.autoTeleport");
        if (config == null) {
            return;
        }

        Player player = event.getPlayer();

        if (!config.getBoolean("join")) {
            return;
        }

        SpawnUtil.teleportSpawn(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        ConfigurationSection config = ConfigSetting.getInstance().getData().getConfigurationSection("spawnSettings.autoTeleport");
        if (config == null) {
            return;
        }

        Player player = event.getPlayer();

        if (!config.getBoolean("respawn")) {
            return;
        }

        SpawnUtil.teleportSpawn(player);
    }
}
