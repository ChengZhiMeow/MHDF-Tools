package cn.chengzhimeow.mhdftools.bukkit.menu.listener;

import cn.chengzhimeow.mhdftools.bukkit.common.menu.AbstractMenu;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class MenuListener implements Listener {
    public static void register(@NotNull Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new MenuListener(), plugin);
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
