package cn.chengzhimeow.mhdftools.bukkit.common.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class ItemStackUtil {
    /**
     * 获取指定物品实例的潜影盒数据实例
     *
     * @param item 物品实例
     * @return 潜影盒数据实例
     */
    public static @Nullable ShulkerBox getShulkerBox(@NotNull ItemStack item) {
        if (!(item.getItemMeta() instanceof BlockStateMeta meta)) return null;
        if (!(meta.getBlockState() instanceof org.bukkit.block.ShulkerBox box)) return null;

        return box;
    }

    /**
     * 获取指定物品实例的显示名称
     *
     * @param item 物品实例
     * @return 组件实例
     */
    public static @NotNull Component getItemName(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (meta.hasCustomName()) return Objects.requireNonNull(meta.customName());
            else if (meta.hasItemName()) return Objects.requireNonNull(meta.itemName());
        }
        return Component.translatable(item.translationKey());
    }
}
