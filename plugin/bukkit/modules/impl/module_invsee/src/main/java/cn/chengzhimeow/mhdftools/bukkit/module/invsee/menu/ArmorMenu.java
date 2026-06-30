package cn.chengzhimeow.mhdftools.bukkit.module.invsee.menu;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionAction;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionActionManager;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.MenuUtil;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.item.MenuItem;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Menu;
import cn.chengzhimeow.mhdftools.bukkit.module.invsee.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.invsee.config.ArmorMenuSetting;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
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
import java.util.Objects;

public final class ArmorMenu extends Menu {
    private final Player target;
    private final Map<String, Integer> equipmentSlotMap = new HashMap<>();
    private final Map<Integer, String> slotEquipmentMap = new HashMap<>();

    public ArmorMenu(Player player, Player target) {
        super(ModuleMain.instance, player);

        this.target = target;
    }

    @Override
    public @NotNull Inventory getInventory() {
        ArmorMenuSetting.Config config = ArmorMenuSetting.getInstance().getConfig();
        NamespacedKey idKey = new NamespacedKey(MHDFToolsBukkit.getInstance(), "id");
        NamespacedKey armorSlotKey = new NamespacedKey(MHDFToolsBukkit.getInstance(), "invsee_armor_slot");
        Inventory inventory = Bukkit.createInventory(this, Math.max(config.slots().size(), 1) * 9, config.title().replace("{player}", this.target.getName()));
        Map<String, Object> params = new HashMap<>();
        params.put("target", this.target);
        params.put("target_name", this.target.getName());

        this.equipmentSlotMap.clear();
        this.slotEquipmentMap.clear();

        for (int row = 0; row < config.slots().size(); row++) {
            String line = config.slots().get(row);
            for (int column = 0; column < Math.min(line.length(), 9); column++) {
                String key = String.valueOf(line.charAt(column));
                if (key.isBlank()) continue;

                int slot = row * 9 + column;
                ItemStack itemStack = null;
                if (config.equipmentSlots().contains(key)) {
                    this.equipmentSlotMap.put(key, slot);
                    this.slotEquipmentMap.put(slot, key);
                    if (config.equipmentSlots().helmet().equals(key))
                        itemStack = this.target.getInventory().getHelmet();
                    if (config.equipmentSlots().chestplate().equals(key))
                        itemStack = this.target.getInventory().getChestplate();
                    if (config.equipmentSlots().leggings().equals(key))
                        itemStack = this.target.getInventory().getLeggings();
                    if (config.equipmentSlots().boots().equals(key)) itemStack = this.target.getInventory().getBoots();
                    if (itemStack != null) {
                        itemStack = itemStack.clone();
                        ItemMeta meta = itemStack.getItemMeta();
                        if (meta != null) {
                            meta.getPersistentDataContainer().set(idKey, PersistentDataType.STRING, key);
                            meta.getPersistentDataContainer().set(armorSlotKey, PersistentDataType.STRING, "true");
                            itemStack.setItemMeta(meta);
                        }
                    }
                }

                if (itemStack == null) {
                    MenuItem item = config.items().get(key);
                    if (item != null && item.checkConditions(super.getPlayer(), params)) {
                        itemStack = MHDFToolsBukkit.getInstance().getItemManager().buildItemStack(item, super.getPlayer());
                    }
                }
                if (itemStack != null) inventory.setItem(slot, itemStack);
            }
        }

        return inventory;
    }

    @Override
    protected void open(InventoryOpenEvent event) {
        Map<String, Object> params = new HashMap<>();
        params.put("target", this.target);
        params.put("target_name", this.target.getName());
        for (ConditionAction action : ArmorMenuSetting.getInstance().getConfig().openActions()) {
            ConditionActionManager.getInstance().actionWithCondition(super.getPlayer(), action, params);
        }
    }

    @Override
    protected void click(InventoryClickEvent event) {
        if (event.isShiftClick()) {
            event.setCancelled(true);
            return;
        }

        int rawSlot = event.getRawSlot();
        if (rawSlot < 0 || rawSlot >= event.getInventory().getSize()) return;

        String equipmentKey = this.slotEquipmentMap.get(rawSlot);
        if (equipmentKey != null) {
            boolean numberKey = event.getClick() == ClickType.NUMBER_KEY;
            ItemStack clickItem = MenuUtil.getClickItem(event);
            ItemStack cursor = numberKey ? super.getPlayer().getInventory().getItem(event.getHotbarButton()) : event.getCursor();
            if (clickItem == null && (cursor == null || cursor.getType().isAir())) return;

            NamespacedKey idKey = new NamespacedKey(MHDFToolsBukkit.getInstance(), "id");
            NamespacedKey armorSlotKey = new NamespacedKey(MHDFToolsBukkit.getInstance(), "invsee_armor_slot");
            boolean clickArmor = false;
            if (clickItem != null) {
                ItemMeta clickMeta = clickItem.getItemMeta();
                if (clickMeta == null) return;

                PersistentDataContainer clickContainer = clickMeta.getPersistentDataContainer();
                String id = clickContainer.get(idKey, PersistentDataType.STRING);
                if (id == null) return;
                if (!ArmorMenuSetting.getInstance().getConfig().items().containsKey(id)) return;
                clickArmor = "true".equals(clickContainer.get(armorSlotKey, PersistentDataType.STRING));
            }

            event.setCancelled(true);
            if (cursor == null || cursor.getType().isAir()) {
                event.getInventory().setItem(rawSlot, null);
            }

            if (cursor != null && !cursor.getType().isAir()) {
                ItemStack slotItem = cursor.clone();
                ItemMeta slotMeta = slotItem.getItemMeta();
                if (slotMeta != null) {
                    slotMeta.getPersistentDataContainer().set(idKey, PersistentDataType.STRING, equipmentKey);
                    slotMeta.getPersistentDataContainer().set(armorSlotKey, PersistentDataType.STRING, "true");
                    slotItem.setItemMeta(slotMeta);
                }
                event.getInventory().setItem(rawSlot, slotItem);
            }

            if (!clickArmor) {
                if (numberKey) super.getPlayer().getInventory().setItem(event.getHotbarButton(), null);
                else super.getPlayer().setItemOnCursor(null);
                return;
            }

            ItemStack cleanClickItem = clickItem.clone();
            ItemMeta cleanMeta = cleanClickItem.getItemMeta();
            if (cleanMeta != null) {
                cleanMeta.getPersistentDataContainer().remove(idKey);
                cleanMeta.getPersistentDataContainer().remove(armorSlotKey);
                cleanClickItem.setItemMeta(cleanMeta);
            }

            if (numberKey) super.getPlayer().getInventory().setItem(event.getHotbarButton(), cleanClickItem);
            else super.getPlayer().setItemOnCursor(cleanClickItem);
            return;
        }

        event.setCancelled(true);

        List<String> rows = ArmorMenuSetting.getInstance().getConfig().slots();
        int row = rawSlot / 9;
        int column = rawSlot % 9;
        if (row >= rows.size()) return;
        if (column >= rows.get(row).length()) return;

        String menuItemKey = String.valueOf(rows.get(row).charAt(column));
        if (menuItemKey.isBlank()) return;

        MenuItem item = ArmorMenuSetting.getInstance().getConfig().items().get(menuItemKey);
        if (item == null) return;

        Map<String, Object> params = new HashMap<>();
        params.put("target", this.target);
        params.put("target_name", this.target.getName());
        item.action(super.getPlayer(), event.getClick(), params);
    }

    @Override
    protected void close(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        ArmorMenuSetting.Config.EquipmentSlots slots = ArmorMenuSetting.getInstance().getConfig().equipmentSlots();
        NamespacedKey idKey = new NamespacedKey(MHDFToolsBukkit.getInstance(), "id");
        NamespacedKey armorSlotKey = new NamespacedKey(MHDFToolsBukkit.getInstance(), "invsee_armor_slot");
        String[] keys = {slots.helmet(), slots.chestplate(), slots.leggings(), slots.boots()};
        ItemStack[] items = new ItemStack[keys.length];

        for (int i = 0; i < keys.length; i++) {
            Integer slot = this.equipmentSlotMap.get(keys[i]);
            ItemStack item = slot == null ? null : inventory.getItem(slot);
            if (item == null) continue;

            ItemMeta meta = item.getItemMeta();
            if (meta == null || !Objects.equals(meta.getPersistentDataContainer().get(armorSlotKey, PersistentDataType.STRING), "true"))
                continue;

            item = item.clone();
            meta = item.getItemMeta();
            if (meta != null) {
                meta.getPersistentDataContainer().remove(idKey);
                meta.getPersistentDataContainer().remove(armorSlotKey);
                item.setItemMeta(meta);
            }
            items[i] = item;
        }

        this.target.getInventory().setHelmet(items[0]);
        this.target.getInventory().setChestplate(items[1]);
        this.target.getInventory().setLeggings(items[2]);
        this.target.getInventory().setBoots(items[3]);

        Map<String, Object> params = new HashMap<>();
        params.put("target", this.target);
        params.put("target_name", this.target.getName());
        for (ConditionAction action : ArmorMenuSetting.getInstance().getConfig().closeActions()) {
            ConditionActionManager.getInstance().actionWithCondition(super.getPlayer(), action, params);
        }
    }
}
