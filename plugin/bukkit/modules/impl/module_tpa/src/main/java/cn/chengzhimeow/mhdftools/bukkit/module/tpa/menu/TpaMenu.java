package cn.chengzhimeow.mhdftools.bukkit.module.tpa.menu;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.AbstractPageMenu;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.TpaMenuSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.service.TpaService;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TpaMenu extends AbstractPageMenu {
    public TpaMenu(Player player, int page) {
        super(player, page);
    }

    @Override
    public int maxPage() {
        int pageSize = this.slots(TpaMenuSetting.getInstance().getConfig().keys().player()).size();
        if (pageSize == 0) return 1;
        return Math.max((int) Math.ceil(BungeeCordManager.getInstance().getPlayerList().size() / (double) pageSize), 1);
    }

    @Override
    protected @NotNull Inventory buildInventory() {
        TpaMenuSetting.Config config = TpaMenuSetting.getInstance().getConfig();
        Inventory inventory = Bukkit.createInventory(this, Math.max(config.slots().size(), 1) * 9, config.title());
        List<String> playerList = BungeeCordManager.getInstance().getPlayerList();
        List<Integer> playerSlots = this.slots(config.keys().player());
        int start = (super.getPage() - 1) * playerSlots.size();
        int end = Math.min(playerList.size(), super.getPage() * playerSlots.size());
        int playerIndex = start;
        Map<String, Object> params = Map.of("page", super.getPage(), "max_page", this.maxPage());
        Map<String, String> pagePlaceholders = Map.of("page", String.valueOf(super.getPage()), "max_page", String.valueOf(this.maxPage()));

        for (int row = 0; row < config.slots().size(); row++) {
            String line = config.slots().get(row);
            for (int column = 0; column < Math.min(line.length(), 9); column++) {
                String key = String.valueOf(line.charAt(column));
                if (key.isBlank()) continue;

                int slot = row * 9 + column;
                if (key.equals(config.keys().player())) {
                    if (playerIndex >= end) continue;
                    inventory.setItem(slot, this.playerItem(playerList.get(playerIndex++)));
                    continue;
                }

                TpaMenuSetting.Config.MenuItem item = config.items().get(key);
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
        NamespacedKey playerKey = new NamespacedKey(MHDFToolsBukkit.getInstance(), "tpa_player");

        String target = container.get(playerKey, PersistentDataType.STRING);
        if (target != null) {
            TpaService.sendRequest(super.getPlayer(), target);
            return;
        }

        String id = container.get(new NamespacedKey(MHDFToolsBukkit.getInstance(), "id"), PersistentDataType.STRING);
        if (id == null) return;

        TpaMenuSetting.Config.MenuItem menuItem = TpaMenuSetting.getInstance().getConfig().items().get(id);
        if (menuItem == null) return;
        menuItem.action(super.getPlayer(), event.getClick(), Map.of("page", super.getPage(), "max_page", this.maxPage()));
    }

    private List<Integer> slots(String key) {
        TpaMenuSetting.Config config = TpaMenuSetting.getInstance().getConfig();
        java.util.ArrayList<Integer> slots = new java.util.ArrayList<>();
        for (int row = 0; row < config.slots().size(); row++) {
            String line = config.slots().get(row);
            for (int column = 0; column < Math.min(line.length(), 9); column++) {
                if (String.valueOf(line.charAt(column)).equals(key)) slots.add(row * 9 + column);
            }
        }
        return slots;
    }

    private ItemStack playerItem(String target) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("page", String.valueOf(super.getPage()));
        placeholders.put("max_page", String.valueOf(this.maxPage()));
        placeholders.put("target", target);

        Map<String, String> pdc = new HashMap<>();
        pdc.put("tpa_player", target);

        ItemStack itemStack = MHDFToolsBukkit.getInstance().getItemManager().buildItemStack(
                TpaMenuSetting.getInstance().getConfig().items().get("player").toBuilderItem(placeholders, pdc),
                super.getPlayer()
        );
        if (itemStack.getType() == Material.PLAYER_HEAD && itemStack.getItemMeta() instanceof SkullMeta meta) {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(target));
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }
}
