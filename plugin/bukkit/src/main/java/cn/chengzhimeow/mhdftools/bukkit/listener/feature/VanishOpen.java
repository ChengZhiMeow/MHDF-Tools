package cn.chengzhimeow.mhdftools.bukkit.listener.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.menu.feature.vanish.BarrelMenu;
import cn.chengzhimeow.mhdftools.bukkit.menu.feature.vanish.ChestMenu;
import cn.chengzhimeow.mhdftools.bukkit.menu.feature.vanish.ShulkerBoxMenu;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

final class VanishOpen extends Listener {
    public VanishOpen() {
        super(
                List.of("vanishSettings.enable")
        );
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        Block block = event.getClickedBlock();

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
        if (!mhdfPlayer.isEnableVanish()) return;

        event.setCancelled(true);

        if (block instanceof Chest chest)
            new ChestMenu(player, chest).openMenu();
        else if (block instanceof ShulkerBox shulkerBox)
            new ShulkerBoxMenu(player, shulkerBox).openMenu();
        else if (block instanceof Barrel barrel)
            new BarrelMenu(player, barrel).openMenu();
    }
}
