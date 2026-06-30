package cn.chengzhimeow.mhdftools.bukkit.module.fly.listener;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.module.fly.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.fly.config.ConfigSetting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

final class AutoChangeFly extends Listener {
    public AutoChangeFly() {
        super(ModuleMain.instance, ConfigSetting.getInstance().getConfig().enable());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!ConfigSetting.getInstance().getConfig().autoEnable().joinServer()) return;
        this.changeFly(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        if (!ConfigSetting.getInstance().getConfig().autoEnable().changeWorld()) return;
        this.changeFly(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!ConfigSetting.getInstance().getConfig().autoEnable().respawn()) return;
        this.changeFly(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!ConfigSetting.getInstance().getConfig().autoDisable().takeHealth()) return;
        if (!(event.getEntity() instanceof Player player)) return;

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
        if (mhdfPlayer.isEnableFly()) mhdfPlayer.disableFly();
    }

    private void changeFly(Player player) {
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
        if (!this.allowFly(player)) mhdfPlayer.disableFly();
        else mhdfPlayer.enableFly();
    }

    private boolean allowFly(Player player) {
        if (ConfigSetting.getInstance().getConfig().autoDisable().worldList().contains(player.getWorld().getName())) return false;

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
        return mhdfPlayer.isEnableFly() || mhdfPlayer.isAllowedFlyingGameMode();
    }
}
