package cn.chengzhimeow.mhdftools.bukkit.compatibility.item;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public final class MythicMobsImpl implements ItemCompatibility {
    private final boolean enable;
    private final String id = ItemCompatibilityIds.MYTHIC_MOBS;

    public MythicMobsImpl() {
        this.enable = Bukkit.getPluginManager().getPlugin("MythicMobs") != null;
    }

    @NotNull
    @Override
    public ItemStack getItemById(@NotNull String id) {
        return MythicBukkit.inst().getItemManager().getItemStack(id);
    }

    @Override
    public @Nullable String getIdByItemStack(@NotNull ItemStack itemStack) {
        return MythicBukkit.inst().getItemManager().getMythicTypeFromItem(itemStack);
    }
}
