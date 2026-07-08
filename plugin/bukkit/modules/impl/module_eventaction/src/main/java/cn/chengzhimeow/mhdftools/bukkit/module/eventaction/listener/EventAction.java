package cn.chengzhimeow.mhdftools.bukkit.module.eventaction.listener;

import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionAction;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionActionManager;
import cn.chengzhimeow.mhdftools.bukkit.module.eventaction.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.eventaction.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.console.LogManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.HashMap;

final class EventAction extends Listener {
    public EventAction() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable()
        );
    }

    /**
     * 执行操作
     *
     * @param player 玩家实例
     * @param event  事件
     */
    public void runAction(Player player, String event) {
        for (ConfigSetting.Config.Action action : ConfigSetting.getInstance().getConfig().actions()) {
            String type = action.event();
            if (type == null) continue;

            LogManager.instance.debug("事件操作类型比对 | 事件名称: {} | 事件类型: {} | 目标类型: {}",
                    action.id(),
                    type,
                    event
            );
            if (!type.equals(event)) continue;

            for (ConditionAction conditionAction : action.actions()) {
                ConditionActionManager.getInstance().actionWithCondition(player, conditionAction, new HashMap<>());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.runAction(event.getPlayer(), "玩家加入服务器");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.runAction(event.getPlayer(), "玩家退出服务器");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        this.runAction(event.getPlayer(), "玩家切换世界");
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        this.runAction(event.getPlayer(), "玩家聊天");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        this.runAction(event.getPlayer(), "玩家死亡");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        this.runAction(event.getPlayer(), "玩家复活");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        this.runAction(event.getPlayer(), "玩家移动");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        this.runAction(event.getPlayer(), "玩家传送");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        this.runAction(event.getPlayer(), "玩家破坏方块");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        this.runAction(event.getPlayer(), "玩家放置方块");
    }
}
