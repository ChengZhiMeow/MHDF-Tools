package cn.chengzhimeow.mhdftools.bukkit.common.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public final class MenuUtil {
    /**
     * 获取指定菜单点击事件中点击的物品实例
     *
     * @param event 菜单点击事件实例
     * @return 物品实例
     */
    public static ItemStack getClickItem(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return null;
        }

        Player player = (Player) event.getWhoClicked();

        ItemStack currentItem = event.getCurrentItem();
        if (event.getClick() == ClickType.NUMBER_KEY) {
            if (currentItem != null) return currentItem;

            ItemStack item = player.getInventory().getItem(event.getHotbarButton());
            if (item == null) return event.getClickedInventory().getItem(event.getRawSlot());
            return item;
        }

        return currentItem;
    }
}
