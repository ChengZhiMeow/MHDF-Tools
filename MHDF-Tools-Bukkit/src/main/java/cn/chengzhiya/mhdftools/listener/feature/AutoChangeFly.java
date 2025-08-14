package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

final class AutoChangeFly extends AbstractListener {
    public AutoChangeFly() {
        super(
                List.of("flySettings.enable")
        );
    }

    /**
     * 加入服务器 自动启用飞行
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // 不处理功能未开启的情况
        if (!Main.instance.getConfigManager().getConfigManager().getData().getBoolean("flySettings.autoEnable.joinServer")) return;

        Player player = event.getPlayer();
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        if (!this.allowFly(player)) mhdfPlayer.disableFly();
        else mhdfPlayer.enableFly();
    }

    /**
     * 切换世界 自动启用飞行
     * 但目标世界在 自动关闭飞行世界列表 中则关闭飞行
     */
    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        // 不处理功能未开启的情况
        if (!Main.instance.getConfigManager().getConfigManager().getData().getBoolean("flySettings.autoEnable.changeWorld")) return;

        Player player = event.getPlayer();
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        if (!this.allowFly(player)) mhdfPlayer.disableFly();
        else mhdfPlayer.enableFly();
    }

    /**
     * 重生 自动启用飞行
     * 但目标世界在 自动关闭飞行世界列表 中则关闭飞行
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // 不处理功能未开启的情况
        if (!Main.instance.getConfigManager().getConfigManager().getData().getBoolean("flySettings.autoEnable.respawn")) return;

        Player player = event.getPlayer();
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        if (!this.allowFly(player)) mhdfPlayer.disableFly();
        else mhdfPlayer.enableFly();
    }

    /**
     * 受伤 自动关闭飞行
     */
    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // 不处理功能未开启的情况
        if (!Main.instance.getConfigManager().getConfigManager().getData().getBoolean("flySettings.autoDisable.takeHealth")) return;
        if (!(event.getEntity() instanceof Player player)) return;

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        if (mhdfPlayer.isEnableFly()) mhdfPlayer.disableFly();
        else mhdfPlayer.enableFly();
    }

    /**
     * 检测是否可以切换飞行
     *
     * @param player 玩家实例
     */
    private boolean allowFly(Player player) {
        // 禁止飞行世界
        if (Main.instance.getConfigManager().getConfigManager().getData().getStringList("flySettings.autoDisable.worldList")
                .contains(player.getWorld().getName())
        ) return false;

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        return mhdfPlayer.isEnableFly() || mhdfPlayer.isAllowedFlyingGameMode();
    }
}
