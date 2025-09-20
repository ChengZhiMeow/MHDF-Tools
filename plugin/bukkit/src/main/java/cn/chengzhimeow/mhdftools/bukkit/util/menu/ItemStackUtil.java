package cn.chengzhimeow.mhdftools.bukkit.util.menu;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.builder.ItemStackBuilder;
import org.bukkit.entity.Player;

import java.util.function.Function;

public final class ItemStackUtil {
    /**
     * 获取指定物品配置实例对应的物品构建实例
     *
     * @param player   玩家实例
     * @param item     物品配置实例
     * @param function 应用的lambda表达式
     * @return 物品构建实例
     */
    public static ItemStackBuilder getItemStackBuilder(Player player, ConfigurationSection item, Function<String, String> function) {
        return new ItemStackBuilder(player, function.apply(item.getString("type")))
                .name(function.apply(item.getString("name")))
                .lore(item.getStringList("lore").stream()
                        .map(function)
                        .toList()
                )
                .amount(item.getInt("amount"))
                .customModelData(item.getInt("customModelData"));
    }

    /**
     * 获取指定物品配置实例对应的物品构建实例
     *
     * @param player 玩家实例
     * @param item   物品配置实例
     * @return 物品构建实例
     */
    public static ItemStackBuilder getItemStackBuilder(Player player, ConfigurationSection item) {
        return ItemStackUtil.getItemStackBuilder(player, item, Function.identity());
    }
}
