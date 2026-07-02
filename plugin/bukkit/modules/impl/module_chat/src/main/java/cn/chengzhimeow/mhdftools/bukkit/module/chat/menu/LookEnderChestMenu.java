package cn.chengzhimeow.mhdftools.bukkit.module.chat.menu;

import cn.chengzhimeow.mhdftools.bukkit.common.menu.AbstractMenu;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.LangSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class LookEnderChestMenu extends AbstractMenu {
    private final Map<Integer, ItemStack> items;

    public LookEnderChestMenu(@NotNull Player player, @NotNull Map<Integer, ItemStack> items) {
        super(player);
        this.items = items;
    }

    @Override
    protected @NotNull Inventory buildInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, ColorUtil.color(LangSetting.getInstance().getConfig().commands().lookEnderChest().title()));
        this.items.forEach((slot, item) -> {
            if (slot >= 0 && slot < inventory.getSize()) inventory.setItem(slot, item);
        });
        return inventory;
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
