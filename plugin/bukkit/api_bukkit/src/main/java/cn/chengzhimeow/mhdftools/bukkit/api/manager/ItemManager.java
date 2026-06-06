package cn.chengzhimeow.mhdftools.bukkit.api.manager;

import cn.chengzhimeow.mhdftools.bukkit.api.entity.BuilderItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ItemManager {
    public abstract @NotNull ItemStack buildItemStack(@NotNull BuilderItem item);

    public abstract @NotNull ItemStack buildItemStack(@NotNull BuilderItem item, @Nullable Player player);
}
