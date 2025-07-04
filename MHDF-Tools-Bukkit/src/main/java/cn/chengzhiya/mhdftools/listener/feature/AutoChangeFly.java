package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

public final class AutoChangeFly extends AbstractListener {
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
        if (!ConfigUtil.getConfig().getBoolean("flySettings.autoEnable.joinServer")) {
            return;
        }

        Player player = event.getPlayer();
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        if (this.allowChange(player)) {
            mhdfPlayer.disableFly();
        }
    }

    /**
     * 切换世界 自动启用飞行
     * 但目标世界在 自动关闭飞行世界列表 中则关闭飞行
     */
    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        // 不处理功能未开启的情况
        if (!ConfigUtil.getConfig().getBoolean("flySettings.autoEnable.changeWorld")) {
            return;
        }

        Player player = event.getPlayer();
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        if (this.allowChange(player)) {
            mhdfPlayer.disableFly();
        }
    }

    /**
     * 重生 自动启用飞行
     * 但目标世界在 自动关闭飞行世界列表 中则关闭飞行
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // 不处理功能未开启的情况
        if (!ConfigUtil.getConfig().getBoolean("flySettings.autoEnable.respawn")) {
            return;
        }

        Player player = event.getPlayer();
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        if (this.allowChange(player)) {
            mhdfPlayer.disableFly();
        }
    }

    /**
     * 受伤 自动关闭飞行
     */
    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // 不处理功能未开启的情况
        if (!ConfigUtil.getConfig().getBoolean("flySettings.autoDisable.takeHealth")) {
            return;
        }

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        if (this.allowChange(player)) {
            mhdfPlayer.disableFly();
        }
    }

    /**
     * 检测是否可以切换飞行
     *
     * @param player 玩家实例
     */
    private boolean allowChange(Player player) {
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        // 目标世界在 自动关闭飞行世界列表 当中
        if (ConfigUtil.getConfig().getStringList("flySettings.autoDisable.worldList").contains(player.getWorld().getName())) {
            mhdfPlayer.disableFly();
            return false;
        }

        // 不处理没有开启飞行的玩家
        if (!mhdfPlayer.isEnableFly()) {
            return false;
        }

        return !mhdfPlayer.isAllowedFlyingGameMode();
    }
}
