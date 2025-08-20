package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.menu.feature.vanish.BarrelMenu;
import cn.chengzhiya.mhdftools.menu.feature.vanish.ChestMenu;
import cn.chengzhiya.mhdftools.menu.feature.vanish.ShulkerBoxMenu;
import org.bukkit.Location;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

final class VanishOpen extends AbstractListener {
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
        Location location = block.getLocation();

        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
            if (!mhdfPlayer.isEnableVanish()) return;

            MHDFScheduler.getRegionScheduler().runTask(Main.instance, location, () -> {
                if (block instanceof Chest chest)
                    new ChestMenu(player, chest).openMenu();
                else if (block instanceof ShulkerBox shulkerBox)
                    new ShulkerBoxMenu(player, shulkerBox).openMenu();
                else if (block instanceof Barrel barrel)
                    new BarrelMenu(player, barrel).openMenu();
            });
        });
    }
}
