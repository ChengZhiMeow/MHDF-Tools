package cn.chengzhimeow.mhdftools.bukkit.api.manager;

import cn.chengzhimeow.mhdftools.bukkit.api.entity.BuilderItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ItemManager {
    @NotNull ItemStack buildItemStack(@NotNull BuilderItem item);

    @NotNull ItemStack buildItemStack(@NotNull BuilderItem item, @Nullable Player player);
}
