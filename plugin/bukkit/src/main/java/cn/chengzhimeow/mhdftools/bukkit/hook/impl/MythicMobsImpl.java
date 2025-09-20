package cn.chengzhimeow.mhdftools.bukkit.hook.impl;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public final class MythicMobsImpl {
    private final MythicBukkit api;

    public MythicMobsImpl() {
        this.api = MythicBukkit.inst();
    }

    /**
     * 获取指定物品ID的物品实例
     *
     * @param id 物品ID
     * @return 物品实例
     */
    public ItemStack getItem(String id) {
        return this.getApi().getItemManager().getItemStack(id);
    }

    /**
     * 获取指定物品实例的物品ID
     *
     * @param itemStack 物品实例
     * @return 物品ID
     */
    public String getItemId(ItemStack itemStack) {
        return this.getApi().getItemManager().getMythicTypeFromItem(itemStack);
    }
}
