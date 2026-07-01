package cn.chengzhimeow.mhdftools.bukkit.module.core.listener;

import cn.chengzhimeow.mhdftools.bukkit.common.menu.AbstractMenu;
import cn.chengzhimeow.mhdftools.bukkit.module.core.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

public final class Menu extends Listener {
    public Menu() {
        super(ModuleMain.instance);
    }

    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof AbstractMenu menu)) return;
        menu.onClick(event);
    }

    @EventHandler
    public void onInventoryOpen(@NotNull InventoryOpenEvent event) {
        if (!(event.getInventory().getHolder() instanceof AbstractMenu menu)) return;
        menu.onOpen(event);
    }

    @EventHandler
    public void onInventoryClose(@NotNull InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof AbstractMenu menu)) return;
        menu.onClose(event);
    }
}
