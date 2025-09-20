package cn.chengzhimeow.mhdftools.bukkit.hook;

import cn.chengzhimeow.mhdftools.bukkit.hook.impl.CraftEngineImpl;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public final class CraftEngineHook extends Hook {
    private CraftEngineImpl api;

    @Override
    public void hook() {
        if (Bukkit.getPluginManager().getPlugin("CraftEngine") != null) {
            this.api = new CraftEngineImpl();
            super.enable = true;
        }
    }

    @Override
    public void unhook() {
        super.enable = false;
        this.api = null;
    }

    /**
     * 获取指定物品ID的物品实例
     *
     * @param item 物品ID
     * @return 物品实例
     */
    public ItemStack getItem(String item) {
        if (super.isEnable()) return this.getApi().getItem(item);
        return new ItemStack(Material.AIR);
    }

    /**
     * 获取指定物品实例的物品ID
     *
     * @param itemStack 物品实例
     * @return 物品ID
     */
    public String getItemId(ItemStack itemStack) {
        if (super.isEnable()) return this.getApi().getItemId(itemStack);
        return null;
    }
}
