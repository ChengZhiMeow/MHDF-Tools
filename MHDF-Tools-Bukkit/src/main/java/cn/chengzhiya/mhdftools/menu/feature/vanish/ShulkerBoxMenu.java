package cn.chengzhiya.mhdftools.menu.feature.vanish;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.menu.Menu;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public final class ShulkerBoxMenu extends Menu {
    private final ShulkerBox shulkerBox;

    public ShulkerBoxMenu(Player player, ShulkerBox shulkerBox) {
        super(
                List.of("vanishSettings.enable"),
                player
        );
        this.shulkerBox = shulkerBox;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, InventoryType.SHULKER_BOX, Main.instance.getConfigManager().getLangManager().i18n("menu.vanish.title"));
        inventory.setContents(shulkerBox.getInventory().getContents());

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
        this.getShulkerBox().getInventory().setContents(inventory.getContents());
    }
}
