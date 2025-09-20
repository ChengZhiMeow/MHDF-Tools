package cn.chengzhimeow.mhdftools.bukkit.hook.impl;

import lombok.Getter;
import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.core.item.CustomItem;
import net.momirealms.craftengine.core.plugin.CraftEngine;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public final class CraftEngineImpl {
    private final CraftEngine api;

    public CraftEngineImpl() {
        this.api = CraftEngine.instance();
    }

    /**
     * 获取指定物品ID的物品实例
     *
     * @param item 物品ID
     * @return 物品实例
     */
    public ItemStack getItem(String item) {
        CustomItem<ItemStack> customItem = CraftEngineItems.byId(Key.of(item));
        if (customItem == null) {
            return new ItemStack(Material.AIR);
        }

        return customItem.buildItemStack();
    }

    /**
     * 获取指定物品实例的物品ID
     *
     * @param itemStack 物品实例
     * @return 物品ID
     */
    public String getItemId(ItemStack itemStack) {
        Key key = this.getApi().itemManager().customItemId(itemStack);
        if (key == null) return null;

        return key.namespace() + ":" + key.value();
    }
}
