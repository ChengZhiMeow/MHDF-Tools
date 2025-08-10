package cn.chengzhiya.mhdftools.menu.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.enums.TeleportRequestType;
import cn.chengzhiya.mhdftools.menu.Menu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.feature.TpaHereUtil;
import cn.chengzhiya.mhdftools.util.feature.TpaUtil;
import cn.chengzhiya.mhdftools.util.menu.MenuUtil;
import io.papermc.paper.persistence.PersistentDataContainerView;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

@Getter
public final class TeleportRequestMenu extends Menu {
    private final YamlConfiguration config;
    private final TeleportRequestType requestType;
    private final int page;

    public TeleportRequestMenu(Player player, TeleportRequestType requestType, int page) {
        super(
                List.of("tpahereSettings.enable"),
                player
        );

        this.config = Main.instance.getConfigManager().getMenuManager().getData(requestType.name().toLowerCase(Locale.ROOT) + ".yml");
        this.requestType = requestType;
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory menu = MenuUtil.createInventory(this, this.getConfig());

        ConfigurationSection items = this.getConfig().getConfigurationSection("items");
        if (items == null) return menu;

        List<String> playerList = Main.instance.getBungeeCordManager().getPlayerList();
        List<Integer> playerSlotList = MenuUtil.getSlotList(items.getConfigurationSection("玩家"));

        int start = (this.getPage() - 1) * playerSlotList.size();
        int maxEnd = this.getPage() * playerSlotList.size();
        int end = Math.min(playerList.size(), maxEnd);

        for (String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);
            if (item == null) {
                continue;
            }

            switch (key) {
                case "玩家" -> {
                    int slot = 0;
                    for (int i = start; i < end; i++) {
                        String target = playerList.get(i);

                        ItemStack itemStack = MenuUtil.getMenuItemStackBuilder(super.getPlayer(), item, s -> this.applyTpaDataString(s, super.getPlayer().getName(), target), key)
                                .persistentDataContainer("target", PersistentDataType.STRING, target)
                                .build();

                        menu.setItem(playerSlotList.get(slot), itemStack);
                        slot++;
                    }
                    continue;
                }
                case "上一页" -> {
                    if (this.page <= 1) {
                        continue;
                    }
                }
                case "下一页" -> {
                    if (playerList.size() <= maxEnd) {
                        continue;
                    }
                }
            }

            MenuUtil.setMenuItem(super.getPlayer(), menu, item, key);
        }

        return menu;
    }

    @Override
    public void open(InventoryOpenEvent event) {
        ActionUtil.runActionList(super.getPlayer(), this.getConfig().getStringList("openActions"));
    }

    @Override
    public void click(InventoryClickEvent event) {
        ItemStack itemStack = MenuUtil.getClickItem(event);
        if (itemStack == null) return;

        event.setCancelled(true);

        PersistentDataContainerView container = itemStack.getPersistentDataContainer();

        String key = container.get(new NamespacedKey(Main.instance, "key"), PersistentDataType.STRING);
        if (key == null) return;

        MenuUtil.runItemClickAction(super.getPlayer(), this.getConfig(), key);

        switch (key) {
            case "玩家" -> {
                String target = container.get(new NamespacedKey(Main.instance, "target"), PersistentDataType.STRING);
                if (target == null) return;

                switch (this.getRequestType()) {
                    case TPAHERE -> TpaHereUtil.sendTpaHereRequest(super.getPlayer(), target);
                    case TPA -> TpaUtil.sendTpaRequest(super.getPlayer(), target);
                }
            }
            case "上一页" ->
                    new TeleportRequestMenu(super.getPlayer(), this.getRequestType(), this.getPage() - 1).openMenu();
            case "下一页" ->
                    new TeleportRequestMenu(super.getPlayer(), this.getRequestType(), this.getPage() + 1).openMenu();
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {
        ActionUtil.runActionList(super.getPlayer(), this.getConfig().getStringList("closeActions"));
    }

    /**
     * 处理家数据实例文本
     *
     * @param message 文本
     * @param player  玩家ID
     * @param target  目标玩家ID
     * @return 处理后的文本
     */
    private String applyTpaDataString(String message, String player, String target) {
        if (message == null) return null;

        return message
                .replace("{player}", player)
                .replace("{target}", target);
    }
}
