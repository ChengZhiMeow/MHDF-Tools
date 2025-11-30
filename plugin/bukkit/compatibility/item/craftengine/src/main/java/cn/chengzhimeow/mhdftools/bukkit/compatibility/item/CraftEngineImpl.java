package cn.chengzhimeow.mhdftools.bukkit.compatibility.item;

import lombok.Getter;
import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.core.item.CustomItem;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public final class CraftEngineImpl implements ItemCompatibility {
    private final boolean enable;
    private final String id = ItemCompatibilityIds.CRAFT_ENGINE;

    public CraftEngineImpl() {
        this.enable = Bukkit.getPluginManager().getPlugin("CraftEngine") != null;
    }

    @Override
    public @NotNull ItemStack getItemById(@NotNull String id) {
        CustomItem<ItemStack> customItem = CraftEngineItems.byId(Key.of(id));
        if (customItem == null) return new ItemStack(Material.AIR);

        return customItem.buildItemStack();
    }

    @Override
    public @Nullable String getIdByItemStack(@NotNull ItemStack itemStack) {
        Key key = CraftEngineItems.getCustomItemId(itemStack);
        if (key == null) return null;

        return key.namespace() + ":" + key.value();
    }
}
