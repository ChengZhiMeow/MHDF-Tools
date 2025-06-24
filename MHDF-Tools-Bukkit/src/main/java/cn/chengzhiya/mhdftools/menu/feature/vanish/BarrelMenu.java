package cn.chengzhiya.mhdftools.menu.feature.vanish;

import cn.chengzhiya.mhdftools.menu.AbstractMenu;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.Barrel;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class BarrelMenu extends AbstractMenu {
    private final Barrel barrel;

    public BarrelMenu(Player player, Barrel barrel) {
        super(
                List.of("vanishSettings.enable"),
                player
        );
        this.barrel = barrel;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, InventoryType.BARREL, LangUtil.i18n("menu.vanish.title"));
        inventory.setContents(barrel.getInventory().getContents());

        return inventory;
    }

    @Override
    public void open(InventoryOpenEvent event) {
    }

    @Override
    public void click(InventoryClickEvent event) {
        saveInventory(event.getInventory());
    }

    @Override
    public void close(InventoryCloseEvent event) {
        saveInventory(event.getInventory());
    }

    private void saveInventory(Inventory inventory) {
        barrel.getInventory().setContents(inventory.getContents());
    }
}
