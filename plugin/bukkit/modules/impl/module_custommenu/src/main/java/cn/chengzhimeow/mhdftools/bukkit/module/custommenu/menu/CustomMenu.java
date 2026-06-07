package cn.chengzhimeow.mhdftools.bukkit.module.custommenu.menu;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionAction;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionActionManager;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.AbstractMenu;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.item.MenuItem;
import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.config.MenuSetting;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CustomMenu extends AbstractMenu {
    private final MenuSetting.Config.Menu config;

    public CustomMenu(@NotNull Player player, @NotNull MenuSetting.Config.Menu config) {
        super(player);
        this.config = config;
    }

    @Override
    protected @NotNull Inventory buildInventory() {
        Inventory inventory = Bukkit.createInventory(this, Math.max(this.config.slots().size(), 1) * 9, this.config.title());
        Player player = this.getPlayer();
        Map<String, Object> params = this.params();

        for (int row = 0; row < this.config.slots().size(); row++) {
            String line = this.config.slots().get(row);
            for (int column = 0; column < Math.min(line.length(), 9); column++) {
                String key = String.valueOf(line.charAt(column));
                if (key.isBlank()) continue;

                MenuItem item = this.config.items().get(key);
                if (item == null || !item.checkConditions(player, params)) continue;

                ItemStack itemStack = MHDFToolsBukkit.getInstance().getItemManager().buildItemStack(item, player);
                inventory.setItem(row * 9 + column, itemStack);
            }
        }

        for (MenuItem item : this.config.items().values()) {
            if (item.getSlot() == null || !item.checkConditions(player, params)) continue;

            ItemStack itemStack = MHDFToolsBukkit.getInstance().getItemManager().buildItemStack(item, player);
            for (int slot : this.parseSlots(item.getSlot(), inventory.getSize())) {
                inventory.setItem(slot, itemStack);
            }
        }

        return inventory;
    }

    @Override
    public void onOpen(@NotNull InventoryOpenEvent event) {
        for (ConditionAction action : this.config.openActions()) {
            ConditionActionManager.getInstance().actionWithCondition(this.getPlayer(), action, this.params());
        }
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        int rawSlot = event.getRawSlot();
        if (rawSlot < 0 || rawSlot >= event.getInventory().getSize()) return;

        event.setCancelled(true);

        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType().isAir()) return;

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        String id = container.get(new NamespacedKey(MHDFToolsBukkit.getInstance(), "id"), PersistentDataType.STRING);
        if (id == null) return;

        MenuItem item = this.config.items().get(id);
        if (item == null) return;

        item.action(this.getPlayer(), event.getClick(), this.params());
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event) {
        for (ConditionAction action : this.config.closeActions()) {
            ConditionActionManager.getInstance().actionWithCondition(this.getPlayer(), action, this.params());
        }
    }

    private @NotNull Map<String, Object> params() {
        Map<String, Object> params = new HashMap<>();
        params.put("menu", this.config.id());
        return params;
    }

    private @NotNull List<Integer> parseSlots(@NotNull String value, int size) {
        return java.util.Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .flatMap(slot -> {
                    if (!slot.contains("-")) return java.util.stream.Stream.of(this.parseSlot(slot, size));

                    String[] parts = slot.split("-", 2);
                    int start = this.parseSlot(parts[0], size);
                    int end = this.parseSlot(parts[1], size);
                    if (start > end) {
                        int temp = start;
                        start = end;
                        end = temp;
                    }
                    return java.util.stream.IntStream.rangeClosed(start, end).boxed();
                })
                .filter(slot -> slot >= 0 && slot < size)
                .toList();
    }

    private int parseSlot(@NotNull String value, int size) {
        try {
            int slot = Integer.parseInt(value.trim());
            return Math.min(Math.max(slot, 0), size - 1);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
