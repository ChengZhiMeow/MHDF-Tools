package cn.chengzhimeow.mhdftools.bukkit.util.menu;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.builder.ItemStackBuilder;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.action.RequirementUtil;
import cn.chengzhimeow.mhdftools.bukkit.message.ColorUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

        if (event.getClick() == ClickType.NUMBER_KEY) {
            ItemStack item = player.getInventory().getItem(event.getHotbarButton());
            if (item == null) return event.getClickedInventory().getItem(event.getRawSlot());
            return item;
        }

        return event.getCurrentItem();
    }

    /**
     * 获取格子列表
     *
     * @param slot 格子文本
     * @return 格子列表
     */
    public static List<Integer> getSlotList(String slot) {
        if (slot == null) return new ArrayList<>();

        List<Integer> slotList = new ArrayList<>();

        if (slot.contains("-")) {
            String[] data = slot.split("-");
            int start = Integer.parseInt(data[0]);
            int end = Integer.parseInt(data[1]);
            for (int i = start; i < (end + 1); i++) {
                slotList.add(i);
            }
        } else {
            slotList.add(Integer.parseInt(slot));
        }

        return slotList;
    }

    /**
     * 获取格子列表
     *
     * @param slots 格子文本列表
     * @return 格子列表
     */
    public static List<Integer> getSlotList(List<String> slots) {
        List<Integer> slotList = new ArrayList<>();

        for (String slot : slots) {
            slotList.addAll(MenuUtil.getSlotList(slot));
        }

        return slotList;
    }

    /**
     * 获取格子列表
     *
     * @param config 配置实例
     * @return 格子列表
     */
    public static List<Integer> getSlotList(ConfigurationSection config) {
        if (config == null) return new ArrayList<>();

        List<Integer> slotList = new ArrayList<>();
        if (!config.getStringList("slots").isEmpty()) {
            slotList.addAll(MenuUtil.getSlotList(config.getStringList("slots")));
        } else {
            slotList.addAll(MenuUtil.getSlotList(config.getString("slot")));
        }

        return slotList;
    }

    /**
     * 为指定玩家实例执行指定物品配置实例的操作
     *
     * @param player 玩家实例
     * @param item   物品配置实例
     */
    public static void runItemClickAction(Player player, ConfigurationSection item) {
        if (item == null) return;

        ConfigurationSection clickRequirementsConfig = item.getConfigurationSection("clickRequirements");
        if (clickRequirementsConfig != null) {
            List<String> denyAction = RequirementUtil.checkRequirements(player, clickRequirementsConfig);
            if (!denyAction.isEmpty()) {
                ActionUtil.runActionList(player, denyAction);
                return;
            }
        }

        List<String> clickAction = item.getStringList("clickActions");
        if (!clickAction.isEmpty()) {
            ActionUtil.runActionList(player, clickAction);
        }
    }

    /**
     * 为指定玩家实例执行指定菜单配置实例下指定物品ID的操作
     *
     * @param player 玩家实例
     * @param menu   菜单配置实例
     * @param key    物品ID
     */
    public static void runItemClickAction(Player player, ConfigurationSection menu, String key) {
        if (menu == null) return;

        ConfigurationSection items = menu.getConfigurationSection("items");
        if (items == null) return;

        MenuUtil.runItemClickAction(player, items.getConfigurationSection(key));
    }

    /**
     * 获取指定物品配置实例的菜单物品构建实例
     *
     * @param player   玩家实例
     * @param item     物品配置实例
     * @param function 应用的lambda表达式
     * @param key      物品ID
     * @return 菜单物品构建实例
     */
    public static ItemStackBuilder getMenuItemStackBuilder(Player player, ConfigurationSection item, Function<String, String> function, String key) {
        return ItemStackUtil.getItemStackBuilder(player, item, function)
                .persistentDataContainer("key", PersistentDataType.STRING, key);
    }

    /**
     * 获取指定物品配置实例的菜单物品构建实例
     *
     * @param player 玩家实例
     * @param item   物品配置实例
     * @param key    物品ID
     * @return 菜单物品构建实例
     */
    public static ItemStackBuilder getMenuItemStackBuilder(Player player, ConfigurationSection item, String key) {
        return MenuUtil.getMenuItemStackBuilder(player, item, Function.identity(), key);
    }

    /**
     * 设置指定菜单实例的指定物品配置实例
     *
     * @param player 玩家实例r
     * @param menu   菜单实例
     * @param item   物品配置实例
     * @param key    物品ID
     */
    public static void setMenuItem(Player player, Inventory menu, ConfigurationSection item, String key) {
        MenuUtil.setMenuItem(menu, item, MenuUtil.getMenuItemStackBuilder(player, item, key).build());
    }

    /**
     * 设置指定菜单实例的指定物品实例
     *
     * @param menu      菜单实例
     * @param item      物品配置实例
     * @param itemStack 物品实例
     */
    public static void setMenuItem(Inventory menu, ConfigurationSection item, ItemStack itemStack) {
        List<Integer> slotList = MenuUtil.getSlotList(item);
        for (Integer slot : slotList) {
            menu.setItem(slot, itemStack);
        }
    }

    /**
     * 创建背包实例
     *
     * @param holder 背包持有者实例
     * @param config 菜单配置实例
     * @return 背包实例
     */
    public static Inventory createInventory(InventoryHolder holder, ConfigurationSection config) {
        int size = config.getInt("size");
        String title = config.getString("title");

        return Bukkit.createInventory(holder, size, title != null ? ColorUtil.color(title) : Component.empty());
    }
}
