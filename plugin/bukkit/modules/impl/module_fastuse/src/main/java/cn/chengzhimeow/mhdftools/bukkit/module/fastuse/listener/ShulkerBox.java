package cn.chengzhimeow.mhdftools.bukkit.module.fastuse.listener;

import cn.chengzhimeow.mhdftools.bukkit.common.menu.ItemStackUtil;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.MenuUtil;
import cn.chengzhimeow.mhdftools.bukkit.module.fastuse.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Menu;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class ShulkerBox extends Listener {
    public ShulkerBox() {
        super(
                ModuleMain.instance,
                List.of("enable")
        );
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.useItemInHand() == Event.Result.DENY) return;

        Player player = event.getPlayer();
        if (!player.hasPermission("mhdftools.fastuse.shulker_box")) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        org.bukkit.block.ShulkerBox box = ItemStackUtil.getShulkerBox(item);
        if (box == null) return;

        Component title = ItemStackUtil.getItemName(item);
        new ShulkerBoxMenu(player, box, title).openMenu();
    }

    private static final class ShulkerBoxMenu extends Menu {
        private final org.bukkit.block.ShulkerBox box;
        private final Component title;

        public ShulkerBoxMenu(Player player, org.bukkit.block.ShulkerBox box, Component title) {
            super(
                    ModuleMain.instance,
                    player
            );

            this.box = box;
            this.title = title;
        }

        @Override
        public @NotNull Inventory getInventory() {
            Inventory inventory = Bukkit.createInventory(this, InventoryType.SHULKER_BOX, title);
            inventory.setContents(this.box.getInventory().getContents());

            return inventory;
        }

        @Override
        protected void click(InventoryClickEvent event) {
            ItemStack item = MenuUtil.getClickItem(event);
            if (item == null) return;

            if (ItemStackUtil.getShulkerBox(item) != null) {
                event.setCancelled(true);
                return;
            }

            this.updateShulker(super.getPlayer(), event.getInventory());
        }

        @Override
        protected void close(InventoryCloseEvent event) {
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
            if (!(item.getItemMeta() instanceof BlockStateMeta meta)) return;
            if (!(meta.getBlockState() instanceof org.bukkit.block.ShulkerBox handBox)) return;
            handBox.getInventory().setContents(inventory.getContents());
            meta.setBlockState(handBox);
            item.setItemMeta(meta);
        }
    }
}
