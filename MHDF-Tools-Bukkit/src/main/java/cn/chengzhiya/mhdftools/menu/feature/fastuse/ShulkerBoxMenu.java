package cn.chengzhiya.mhdftools.menu.feature.fastuse;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.menu.Menu;
import cn.chengzhiya.mhdftools.util.menu.MenuUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ShulkerBoxMenu extends Menu {
    public ShulkerBoxMenu(Player player) {
        super(
                List.of("fastUseSettings.enable"),
                player
        );
    }

    @Override
    public @NotNull Inventory getInventory() {
        ItemStack item = getPlayer().getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        BlockStateMeta blockMate = (BlockStateMeta) meta;
        ShulkerBox box = (ShulkerBox) blockMate.getBlockState();

        Inventory inventory = Bukkit.createInventory(this, InventoryType.BARREL, Main.instance.getConfigManager().getLangManager().i18n("menu.fastuse.shulkerBox.title"));
        inventory.setContents(box.getInventory().getContents());

        return inventory;
    }

    @Override
    public void open(InventoryOpenEvent event) {
    }

    @Override
    public void click(InventoryClickEvent event) {
        ItemStack item = MenuUtil.getClickItem(event);
        if (item == null) {
            return;
        }

        if (item.getType().toString().contains("SHULKER_BOX")) {
            event.setCancelled(true);
            return;
        }

        this.updateShulker(super.getPlayer(), event.getInventory());
    }

    @Override
    public void close(InventoryCloseEvent event) {
        this.updateShulker(super.getPlayer(), event.getInventory());
    }

    /**
     * 更新手中潜影盒
     *
     * @param player    玩家实例
     * @param inventory 潜影盒背包实例
     */
    private void updateShulker(Player player, Inventory inventory) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!item.getType().toString().contains("SHULKER_BOX")) {
            player.closeInventory();
            return;
        }

        if (item.getItemMeta() instanceof BlockStateMeta blockStateMeta) {
            ShulkerBox box = (ShulkerBox) blockStateMeta.getBlockState();

            box.getInventory().setContents(inventory.getContents());

            blockStateMeta.setBlockState(box);
            item.setItemMeta(blockStateMeta);
            player.getInventory().setItemInMainHand(item);
        }
    }
}
