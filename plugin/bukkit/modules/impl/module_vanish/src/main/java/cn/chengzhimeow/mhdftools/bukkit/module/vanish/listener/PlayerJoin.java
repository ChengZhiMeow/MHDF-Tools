package cn.chengzhimeow.mhdftools.bukkit.module.vanish.listener;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.database.data.VanishStatus;
import cn.chengzhimeow.mhdftools.api.manager.feature.VanishStatusManager;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.config.BossBarSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.config.ConfigSetting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

final class PlayerJoin extends Listener {
    public PlayerJoin() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable()
        );
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Set<UUID> hiddenPlayerIds = this.getHiddenPlayerIds();
        if (hiddenPlayerIds.isEmpty()) return;

        if (hiddenPlayerIds.contains(player.getUniqueId())) {
            if (ConfigSetting.getInstance().getConfig().bossbar().vanish().enable()) {
                player.showBossBar(BossBarSetting.getInstance().getVanish());
            }

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.hidePlayer(ModuleMain.instance.getPlugin(), player);
            }
        }

        for (UUID uuid : hiddenPlayerIds) {
            if (uuid.equals(player.getUniqueId())) continue;

            Player target = Bukkit.getPlayer(uuid);
            if (target == null) continue;

            player.hidePlayer(ModuleMain.instance.getPlugin(), target);
        }
    }

    private Set<UUID> getHiddenPlayerIds() {
        VanishStatusManager manager = MHDFToolsAPI.getInstance().getVanishStatusManager();
        if (!manager.isEnable()) return Set.of();

        return manager.getList().stream()
                .filter(VanishStatus::isEnable)
                .map(VanishStatus::getPlayer)
                .collect(Collectors.toSet());
    }
}
