package cn.chengzhiya.mhdftools.menu.feature.vanish;

import cn.chengzhiya.mhdftools.menu.AbstractMenu;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
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

public final class ChestMenu extends AbstractMenu {
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
        Inventory inventory = Bukkit.createInventory(this, chest.getInventory().getSize(), LangUtil.i18n("menu.vanish.title"));
        inventory.setContents(chest.getInventory().getContents());

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
        if (chest.getInventory().getHolder() instanceof DoubleChest doubleChest) {
            Objects.requireNonNull(doubleChest.getLeftSide()).getInventory().setContents(inventory.getContents());
            Objects.requireNonNull(doubleChest.getRightSide()).getInventory().setContents(inventory.getContents());
        } else {
            chest.getInventory().setContents(inventory.getContents());
        }
    }
}
