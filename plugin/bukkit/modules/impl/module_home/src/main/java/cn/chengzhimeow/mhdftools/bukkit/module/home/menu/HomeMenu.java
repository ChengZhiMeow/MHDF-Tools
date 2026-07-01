package cn.chengzhimeow.mhdftools.bukkit.module.home.menu;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.HomeData;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.AbstractPageMenu;
import cn.chengzhimeow.mhdftools.bukkit.module.home.config.HomeMenuSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.home.config.LangSetting;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HomeMenu extends AbstractPageMenu {
    public HomeMenu(Player player, int page) {
        super(player, page);
    }

    @Override
    public int maxPage() {
        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(super.getPlayer().getUniqueId(), super.getPlayer().getName());
        int pageSize = this.slots(HomeMenuSetting.getInstance().getConfig().keys().home()).size();
        if (pageSize == 0) return 1;
        return Math.max((int) Math.ceil(player.getHomeList().size() / (double) pageSize), 1);
    }

    @Override
    protected @NotNull Inventory buildInventory() {
        HomeMenuSetting.Config config = HomeMenuSetting.getInstance().getConfig();
        Inventory inventory = Bukkit.createInventory(this, Math.max(config.slots().size(), 1) * 9, config.title());
        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(super.getPlayer().getUniqueId(), super.getPlayer().getName());
        List<HomeData> homeList = player.getHomeList();
        List<Integer> homeSlots = this.slots(config.keys().home());
        int start = (super.getPage() - 1) * homeSlots.size();
        int end = Math.min(homeList.size(), super.getPage() * homeSlots.size());
        int homeIndex = start;

        for (int row = 0; row < config.slots().size(); row++) {
            String line = config.slots().get(row);
            for (int column = 0; column < Math.min(line.length(), 9); column++) {
                String key = String.valueOf(line.charAt(column));
                if (key.isBlank()) continue;

                int slot = row * 9 + column;
                if (key.equals(config.keys().home())) {
                    if (homeIndex >= end) continue;
                    inventory.setItem(slot, this.homeItem(homeList.get(homeIndex++)));
                    continue;
                }
                if (key.equals(config.keys().previousPage())) {
                    if (super.getPage() > 1)
                        inventory.setItem(slot, this.pageItem("previous_page", super.getPage() - 1));
                    continue;
                }
                if (key.equals(config.keys().nextPage())) {
                    if (end < homeList.size()) inventory.setItem(slot, this.pageItem("next_page", super.getPage() + 1));
                    continue;
                }

                HomeMenuSetting.Config.MenuItem item = config.items().get(key);
                if (item == null) continue;
                inventory.setItem(slot, MHDFToolsBukkit.getInstance().getItemManager().buildItemStack(item.toBuilderItem(Map.of(), Map.of()), super.getPlayer()));
            }
        }

        return inventory;
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType().isAir()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey homeKey = new NamespacedKey(MHDFToolsBukkit.getInstance(), "home");
        NamespacedKey pageKey = new NamespacedKey(MHDFToolsBukkit.getInstance(), "home_page");

        String home = container.get(homeKey, PersistentDataType.STRING);
        if (home != null) {
            MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(super.getPlayer().getUniqueId(), super.getPlayer().getName());
            if (!player.hasHome(home)) return;

            player.teleport(player.getHome(home).toBungeeCordLocation());
            player.sendMessage(LangSetting.getInstance().getConfig().commands().home().message()
                    .replace("{home}", home));
            return;
        }

        String page = container.get(pageKey, PersistentDataType.STRING);
        if (page == null) return;
        new HomeMenu(super.getPlayer(), Integer.parseInt(page)).openInventory();
    }

    private List<Integer> slots(String key) {
        HomeMenuSetting.Config config = HomeMenuSetting.getInstance().getConfig();
        java.util.ArrayList<Integer> slots = new java.util.ArrayList<>();
        for (int row = 0; row < config.slots().size(); row++) {
            String line = config.slots().get(row);
            for (int column = 0; column < Math.min(line.length(), 9); column++) {
                if (String.valueOf(line.charAt(column)).equals(key)) slots.add(row * 9 + column);
            }
        }
        return slots;
    }

    private ItemStack homeItem(HomeData data) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("name", data.getHome());
        placeholders.put("server", data.getServer());
        placeholders.put("world", data.getWorld());
        placeholders.put("x", String.valueOf(data.getX()));
        placeholders.put("y", String.valueOf(data.getY()));
        placeholders.put("z", String.valueOf(data.getZ()));
        placeholders.put("yaw", String.valueOf(data.getYaw()));
        placeholders.put("pitch", String.valueOf(data.getPitch()));

        Map<String, String> pdc = new HashMap<>();
        pdc.put("home", data.getHome());

        return MHDFToolsBukkit.getInstance().getItemManager().buildItemStack(
                HomeMenuSetting.getInstance().getConfig().items().get("home").toBuilderItem(placeholders, pdc),
                super.getPlayer()
        );
    }

    private ItemStack pageItem(String id, int page) {
        Map<String, String> pdc = new HashMap<>();
        pdc.put("home_page", String.valueOf(page));

        return MHDFToolsBukkit.getInstance().getItemManager().buildItemStack(
                HomeMenuSetting.getInstance().getConfig().items().get(id).toBuilderItem(Map.of(), pdc),
                super.getPlayer()
        );
    }
}
