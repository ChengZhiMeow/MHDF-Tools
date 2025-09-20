package cn.chengzhimeow.mhdftools.bukkit.listener.feature;

import cn.chengzhimeow.mhdftools.bukkit.listener.AbstractListener;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.EventActionUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.List;

final class EventAction extends AbstractListener {
    public EventAction() {
        super(
                List.of("eventActionSettings.enable")
        );
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        EventActionUtil.runAction(event.getPlayer(), "玩家加入服务器");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        EventActionUtil.runAction(event.getPlayer(), "玩家退出服务器");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        EventActionUtil.runAction(event.getPlayer(), "玩家切换世界");
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        EventActionUtil.runAction(event.getPlayer(), "玩家聊天");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        EventActionUtil.runAction(event.getPlayer(), "玩家死亡");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        EventActionUtil.runAction(event.getPlayer(), "玩家复活");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        EventActionUtil.runAction(event.getPlayer(), "玩家移动");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        EventActionUtil.runAction(event.getPlayer(), "玩家传送");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        EventActionUtil.runAction(event.getPlayer(), "玩家破坏方块");
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        EventActionUtil.runAction(event.getPlayer(), "玩家放置方块");
    }
}
