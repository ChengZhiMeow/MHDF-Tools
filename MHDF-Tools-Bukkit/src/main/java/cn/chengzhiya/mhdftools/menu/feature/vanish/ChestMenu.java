package cn.chengzhiya.mhdftools.menu.feature.vanish;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.menu.Menu;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@Getter
public final class ChestMenu extends Menu {
    private final Chest chest;

    public ChestMenu(Player player, Chest chest) {
        super(
                List.of("vanishSettings.enable"),
                player
        );
        this.chest = chest;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, chest.getInventory().getSize(), Main.instance.getConfigManager().getLangManager().i18n("menu.vanish.title"));
        inventory.setContents(chest.getInventory().getContents());

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
        if (this.getChest().getInventory().getHolder() instanceof DoubleChest doubleChest) {
            Objects.requireNonNull(doubleChest.getLeftSide()).getInventory().setContents(inventory.getContents());
            Objects.requireNonNull(doubleChest.getRightSide()).getInventory().setContents(inventory.getContents());
        } else {
            this.getChest().getInventory().setContents(inventory.getContents());
        }
    }
}
