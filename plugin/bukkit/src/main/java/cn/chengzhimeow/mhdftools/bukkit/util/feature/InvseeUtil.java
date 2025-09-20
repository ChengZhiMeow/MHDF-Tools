package cn.chengzhimeow.mhdftools.bukkit.util.feature;

import cn.chengzhimeow.mhdftools.bukkit.menu.feature.ArmorMenu;
import org.bukkit.entity.Player;

public final class InvseeUtil {
    /**
     * 打开指定玩家的指定类型背包
     *
     * @param player        玩家实例
     * @param target        被打开的玩家实例
     * @param inventoryType 背包类型
     * @return 背包类型是否存在
     */
    public static boolean invsee(Player player, Player target, String inventoryType) {
        switch (inventoryType) {
            case "inventory" -> player.openInventory(target.getInventory());
            case "enderchest" -> player.openInventory(target.getEnderChest());
            case "armor" -> new ArmorMenu(player, target).openMenu();
            default -> {
                return false;
            }
        }

        return true;
    }
}
