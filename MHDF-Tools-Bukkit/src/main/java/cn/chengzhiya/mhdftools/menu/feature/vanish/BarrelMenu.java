package cn.chengzhiya.mhdftools.menu.feature.vanish;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.menu.Menu;
import lombok.Getter;
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

@Getter
public final class BarrelMenu extends Menu {
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
        Inventory inventory = Bukkit.createInventory(this, InventoryType.BARREL, Main.instance.getConfigManager().getLangManager().i18n("menu.vanish.title"));
        inventory.setContents(this.barrel.getInventory().getContents());

        return inventory;
    }

    @Override
    public void open(InventoryOpenEvent event) {
    }

    @Override
    public void click(InventoryClickEvent event) {
        this.saveInventory(event.getInventory());
    }

    @Override
    public void close(InventoryCloseEvent event) {
        this.saveInventory(event.getInventory());
    }

    private void saveInventory(Inventory inventory) {
        this.getBarrel().getInventory().setContents(inventory.getContents());
    }
}
