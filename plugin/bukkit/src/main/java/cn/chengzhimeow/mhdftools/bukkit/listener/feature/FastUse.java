package cn.chengzhimeow.mhdftools.bukkit.listener.feature;

import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.menu.feature.fastuse.ShulkerBoxMenu;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

final class FastUse extends Listener {
    public FastUse() {
        super(
                List.of("fastUseSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        // 潜影盒
        if (item.getType().toString().endsWith("SHULKER_BOX")) {
            // 不处理功能未开启的情况
            if (!ConfigSetting.getInstance().getData().getBoolean("fastUseSettings.shulkerBox")) {
                return;
            }

            // 不处理没有权限的情况
            if (!player.hasPermission("mhdftools.fastuse.shulkerbox")) {
                return;
            }

            new ShulkerBoxMenu(player).openMenu();
            return;
        }

        // 末影箱
        if (item.getType() == Material.ENDER_CHEST) {
            // 不处理功能未开启的情况
            if (!ConfigSetting.getInstance().getData().getBoolean("fastUseSettings.enderChest")) {
                return;
            }

            // 不处理没有权限的情况
            if (!player.hasPermission("mhdftools.fastuse.enderchest")) {
                return;
            }

            player.openInventory(player.getEnderChest());
            return;
        }

        // 工作台
        if (item.getType() == Material.CRAFTING_TABLE) {
            // 不处理功能未开启的情况
            if (!ConfigSetting.getInstance().getData().getBoolean("fastUseSettings.craftingTable")) {
                return;
            }

            // 不处理没有权限的情况
            if (!player.hasPermission("mhdftools.fastuse.craftingtable")) {
                return;
            }

            player.openWorkbench(null, true);
        }
    }
}
