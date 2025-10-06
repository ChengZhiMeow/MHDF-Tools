package cn.chengzhimeow.mhdftools.bukkit.compatibility.item;

import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.core.item.CustomItem;
import net.momirealms.craftengine.core.plugin.CraftEngine;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class CraftEngineImpl implements Compatibility {
    private final CraftEngine api;

    public CraftEngineImpl() {
        this.api = CraftEngine.instance();
    }

    @Override
    public ItemStack getItemById(String id) {
        CustomItem<ItemStack> customItem = CraftEngineItems.byId(Key.of(id));
        if (customItem == null) {
            return new ItemStack(Material.AIR);
        }

        return customItem.buildItemStack();
    }

    @Override
    public String getIdByItemStack(ItemStack itemStack) {
        Key key = this.api.itemManager().customItemId(itemStack);
        if (key == null) return null;

        return key.namespace() + ":" + key.value();
    }
}
