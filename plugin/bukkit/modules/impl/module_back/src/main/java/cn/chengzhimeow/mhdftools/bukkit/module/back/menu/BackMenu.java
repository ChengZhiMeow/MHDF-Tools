package cn.chengzhimeow.mhdftools.bukkit.module.back.menu;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.BackData;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.AbstractPageMenu;
import cn.chengzhimeow.mhdftools.bukkit.module.back.config.BackMenuSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.back.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.back.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.back.service.BackService;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BackMenu extends AbstractPageMenu {
    public BackMenu(Player player, int page) {
        super(player, page);
    }

    @Override
    public int maxPage() {
        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(super.getPlayer().getUniqueId(), super.getPlayer().getName());
        int pageSize = this.slots(BackMenuSetting.getInstance().getConfig().keys().location()).size();
        if (pageSize == 0) return 1;
        return Math.max((int) Math.ceil(player.getBackDataList(BackService.getMaxBack(super.getPlayer())).size() / (double) pageSize), 1);
    }

    @Override
    protected @NotNull Inventory buildInventory() {
        BackMenuSetting.Config config = BackMenuSetting.getInstance().getConfig();
        Inventory inventory = Bukkit.createInventory(this, Math.max(config.slots().size(), 1) * 9, config.title());
        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(super.getPlayer().getUniqueId(), super.getPlayer().getName());
        List<BackData> backList = player.getBackDataList(BackService.getMaxBack(super.getPlayer()));
        List<Integer> backSlots = this.slots(config.keys().location());
        int start = (super.getPage() - 1) * backSlots.size();
        int end = Math.min(backList.size(), super.getPage() * backSlots.size());
        int backIndex = start;
        Map<String, Object> params = Map.of("page", super.getPage(), "max_page", this.maxPage());
        Map<String, String> pagePlaceholders = Map.of("page", String.valueOf(super.getPage()), "max_page", String.valueOf(this.maxPage()));

        for (int row = 0; row < config.slots().size(); row++) {
            String line = config.slots().get(row);
            for (int column = 0; column < Math.min(line.length(), 9); column++) {
                String key = String.valueOf(line.charAt(column));
                if (key.isBlank()) continue;

                int slot = row * 9 + column;
                if (key.equals(config.keys().location())) {
                    if (backIndex >= end) continue;
                    inventory.setItem(slot, this.backItem(backList.get(backIndex++)));
                    continue;
                }

                BackMenuSetting.Config.MenuItem item = config.items().get(key);
                if (item == null) {
                    item = config.items().entrySet().stream()
                            .filter(entry -> entry.getKey().startsWith(key + "_"))
                            .map(Map.Entry::getValue)
                            .filter(menuItem -> menuItem.checkConditions(super.getPlayer(), params))
                            .findFirst()
                            .orElse(null);
                } else if (!item.checkConditions(super.getPlayer(), params)) {
                    item = null;
                }
                if (item == null) continue;
                inventory.setItem(slot, MHDFToolsBukkit.getInstance().getItemManager().buildItemStack(item.toBuilderItem(pagePlaceholders, Map.of()), super.getPlayer()));
            }
        }

        return inventory;
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
        if (ConfigSetting.getInstance().getConfig().blackWorld().contains(super.getPlayer().getWorld().getName())) {
            super.getPlayer().sendMessage(GlobalLangSetting.getInstance().getConfig().blackWorld());
            return;
        }

        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType().isAir()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey backKey = new NamespacedKey(MHDFToolsBukkit.getInstance(), "back_id");

        String id = container.get(backKey, PersistentDataType.STRING);
        if (id != null) {
            MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(super.getPlayer().getUniqueId(), super.getPlayer().getName());
            BackData backData = player.getBackData(Integer.parseInt(id));
            player.teleport(backData.toBungeeCordLocation());
            player.sendMessage(LangSetting.getInstance().getConfig().commands().back().message());
            return;
        }

        String itemId = container.get(new NamespacedKey(MHDFToolsBukkit.getInstance(), "id"), PersistentDataType.STRING);
        if (itemId == null) return;

        BackMenuSetting.Config.MenuItem menuItem = BackMenuSetting.getInstance().getConfig().items().get(itemId);
        if (menuItem == null) return;
        menuItem.action(super.getPlayer(), event.getClick(), Map.of("page", super.getPage(), "max_page", this.maxPage()));
    }

    private List<Integer> slots(String key) {
        BackMenuSetting.Config config = BackMenuSetting.getInstance().getConfig();
        java.util.ArrayList<Integer> slots = new java.util.ArrayList<>();
        for (int row = 0; row < config.slots().size(); row++) {
            String line = config.slots().get(row);
            for (int column = 0; column < Math.min(line.length(), 9); column++) {
                if (String.valueOf(line.charAt(column)).equals(key)) slots.add(row * 9 + column);
            }
        }
        return slots;
    }

    private ItemStack backItem(BackData data) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(data.getTime()), ZoneId.systemDefault());
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("page", String.valueOf(super.getPage()));
        placeholders.put("max_page", String.valueOf(this.maxPage()));
        placeholders.put("year", String.valueOf(dateTime.getYear()));
        placeholders.put("month", String.valueOf(dateTime.getMonthValue()));
        placeholders.put("day", String.valueOf(dateTime.getDayOfMonth()));
        placeholders.put("hour", String.valueOf(dateTime.getHour()));
        placeholders.put("minute", String.valueOf(dateTime.getMinute()));
        placeholders.put("second", String.valueOf(dateTime.getSecond()));
        placeholders.put("type", LangSetting.getInstance().getConfig().commands().back().typeName(data.getType()));
        placeholders.put("server", data.getServer());
        placeholders.put("world", data.getWorld());
        placeholders.put("x", String.valueOf(data.getX()));
        placeholders.put("y", String.valueOf(data.getY()));
        placeholders.put("z", String.valueOf(data.getZ()));
        placeholders.put("yaw", String.valueOf(data.getYaw()));
        placeholders.put("pitch", String.valueOf(data.getPitch()));

        Map<String, String> pdc = new HashMap<>();
        pdc.put("back_id", String.valueOf(data.getId()));

        return MHDFToolsBukkit.getInstance().getItemManager().buildItemStack(
                BackMenuSetting.getInstance().getConfig().items().get("location").toBuilderItem(placeholders, pdc),
                super.getPlayer()
        );
    }
}
