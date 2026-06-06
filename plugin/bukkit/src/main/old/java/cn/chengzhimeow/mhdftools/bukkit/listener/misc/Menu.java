package cn.chengzhimeow.mhdftools.bukkit.listener.misc;

import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

final class Menu extends Listener {
    public Menu() {
        super();
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (holder instanceof cn.chengzhimeow.mhdftools.bukkit.menu.Menu menu) {
            menu.onOpen(event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder inventoryHolder = inventory.getHolder();

        if (inventoryHolder instanceof cn.chengzhimeow.mhdftools.bukkit.menu.Menu menu) {
            menu.onClick(event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder inventoryHolder = inventory.getHolder();

        if (inventoryHolder instanceof cn.chengzhimeow.mhdftools.bukkit.menu.Menu menu) {
            menu.onClose(event);
        }
    }
}
