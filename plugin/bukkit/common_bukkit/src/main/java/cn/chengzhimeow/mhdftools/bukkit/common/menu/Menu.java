package cn.chengzhimeow.mhdftools.bukkit.common.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

public abstract class Menu {
    public abstract void onClick(@NotNull InventoryClickEvent event);

    public abstract void onOpen(@NotNull InventoryOpenEvent event);

    public abstract void onClose(@NotNull InventoryCloseEvent event);
}
