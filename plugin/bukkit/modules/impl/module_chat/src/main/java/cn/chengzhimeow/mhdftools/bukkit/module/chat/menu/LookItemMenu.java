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

public final class LookItemMenu extends AbstractMenu {
    private final ItemStack item;

    public LookItemMenu(@NotNull Player player, @NotNull ItemStack item) {
        super(player);
        this.item = item;
    }

    @Override
    protected @NotNull Inventory buildInventory() {
        Inventory inventory = Bukkit.createInventory(this, 9, ColorUtil.color(LangSetting.getInstance().getConfig().commands().lookItem().title()));
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItem(slot, this.item);
        }
        return inventory;
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
