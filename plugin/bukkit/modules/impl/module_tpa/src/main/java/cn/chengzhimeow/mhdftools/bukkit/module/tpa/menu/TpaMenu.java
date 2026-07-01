package cn.chengzhimeow.mhdftools.bukkit.module.tpa.menu;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.AbstractPageMenu;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.tpa.config.TpaMenuSetting;
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
                if (key.equals(config.keys().previousPage())) {
                    if (super.getPage() > 1)
                        inventory.setItem(slot, this.pageItem("previous_page", super.getPage() - 1));
                    continue;
                }
                if (key.equals(config.keys().nextPage())) {
                    if (end < playerList.size())
                        inventory.setItem(slot, this.pageItem("next_page", super.getPage() + 1));
                    continue;
                }

                TpaMenuSetting.Config.MenuItem item = config.items().get(key);
                if (item == null) continue;
                inventory.setItem(slot, MHDFToolsBukkit.getInstance().getItemManager().buildItemStack(item.toBuilderItem(Map.of(), Map.of()), super.getPlayer()));
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
        NamespacedKey pageKey = new NamespacedKey(MHDFToolsBukkit.getInstance(), "tpa_page");

        String target = container.get(playerKey, PersistentDataType.STRING);
        if (target != null) {
            this.sendRequest(target);
            return;
        }

        String page = container.get(pageKey, PersistentDataType.STRING);
        if (page == null) return;
        new TpaMenu(super.getPlayer(), Integer.parseInt(page)).openInventory();
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
        placeholders.put("target", target);

        Map<String, String> pdc = new HashMap<>();
        pdc.put("tpa_player", target);

        return MHDFToolsBukkit.getInstance().getItemManager().buildItemStack(
                TpaMenuSetting.getInstance().getConfig().items().get("player").toBuilderItem(placeholders, pdc),
                super.getPlayer()
        );
    }

    private ItemStack pageItem(String id, int page) {
        Map<String, String> pdc = new HashMap<>();
        pdc.put("tpa_page", String.valueOf(page));

        return MHDFToolsBukkit.getInstance().getItemManager().buildItemStack(
                TpaMenuSetting.getInstance().getConfig().items().get(id).toBuilderItem(Map.of(), pdc),
                super.getPlayer()
        );
    }

    private void sendRequest(String targetName) {
        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(super.getPlayer().getUniqueId(), super.getPlayer().getName());
        if (!BungeeCordManager.getInstance().ifPlayerOnline(targetName)) {
            super.getPlayer().sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
            return;
        }

        if (targetName.equals(super.getPlayer().getName())) {
            super.getPlayer().sendMessage(LangSetting.getInstance().getConfig().commands().tpa().sendSelf());
            return;
        }

        String delay = ModuleMain.instance.getDelayCache().get(super.getPlayer().getName());
        if (delay != null) {
            super.getPlayer().sendMessage(LangSetting.getInstance().getConfig().commands().tpa().inDelay()
                    .replace("{delay}", delay));
            return;
        }

        ModuleMain.instance.getRequestCache().put(super.getPlayer().getName(), targetName);
        ModuleMain.instance.getDelayCache().put(super.getPlayer().getName(), String.valueOf(ConfigSetting.getInstance().getConfig().delay()));
        super.getPlayer().sendMessage(LangSetting.getInstance().getConfig().commands().tpa().message()
                .replace("{player}", targetName));

        MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(targetName);
        if (target.isIgnore(player)) return;

        target.sendMessage(LangSetting.getInstance().getConfig().commands().tpa().requestMessage()
                .replaceByMiniMessage("{player}", super.getPlayer().getName()));
    }
}
