package cn.chengzhimeow.mhdftools.bukkit.compatibility.item;

import org.bukkit.inventory.ItemStack;

public interface Compatibility {
    ItemStack getItemById(String id);

    String getIdByItemStack(ItemStack itemStack);

    class Ids {
        public static String CRAFT_ENGINE = "craft_engine";
        public static String MYTHIC_MOBS = "mythic_mobs";
    }
}
