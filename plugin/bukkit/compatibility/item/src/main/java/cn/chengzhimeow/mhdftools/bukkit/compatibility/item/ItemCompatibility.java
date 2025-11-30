package cn.chengzhimeow.mhdftools.bukkit.compatibility.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemCompatibility {
    boolean isEnable();

    @NotNull String getId();

    @NotNull ItemStack getItemById(@NotNull String id);

    @Nullable String getIdByItemStack(@NotNull ItemStack itemStack);

    class ItemCompatibilityIds {
        public static String CRAFT_ENGINE = "craftengine";
        public static String MYTHIC_MOBS = "mythicmobs";
    }
}
