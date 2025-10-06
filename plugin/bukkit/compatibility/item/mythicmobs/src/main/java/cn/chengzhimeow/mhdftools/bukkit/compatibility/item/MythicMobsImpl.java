package cn.chengzhimeow.mhdftools.bukkit.compatibility.item;

import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.inventory.ItemStack;

public final class MythicMobsImpl implements Compatibility {
    private final MythicBukkit api;

    public MythicMobsImpl() {
        this.api = MythicBukkit.inst();
    }

    @Override
    public ItemStack getItemById(String id) {
        return this.api.getItemManager().getItemStack(id);
    }

    @Override
    public String getIdByItemStack(ItemStack itemStack) {
        return this.api.getItemManager().getMythicTypeFromItem(itemStack);
    }
}
