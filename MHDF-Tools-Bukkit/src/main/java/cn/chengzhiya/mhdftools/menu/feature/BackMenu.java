package cn.chengzhiya.mhdftools.menu.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.BackData;
import cn.chengzhiya.mhdftools.menu.AbstractMenu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.feature.BackUtil;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Getter
public final class BackMenu extends AbstractMenu {
    private final YamlConfiguration config;
    private final int page;

    public BackMenu(Player player, int page) {
        super(
                List.of("backSettings.enable"),
                player
        );

        this.config = Main.instance.getConfigManager().getMenuManager().getData("back.yml");
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory menu = MenuUtil.createInventory(this, getConfig());

        ConfigurationSection items = getConfig().getConfigurationSection("items");
        if (items == null) {
            return menu;
        }

        MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(this.getPlayer());
        List<BackData> backList = player.getBackDataList(BackUtil.getMaxBack(this.getPlayer()));
        List<Integer> backSlotList = MenuUtil.getSlotList(items.getConfigurationSection("位置"));

        int start = (page - 1) * backSlotList.size();
        int maxEnd = page * backSlotList.size();
        int end = Math.min(backList.size(), maxEnd);

        for (String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);
            if (item == null) {
                continue;
            }

            switch (key) {
                case "位置" -> {
                    int slot = 0;
                    for (int i = start; i < end; i++) {
                        BackData backData = backList.get(i);

                        ItemStack itemStack = MenuUtil.getMenuItemStackBuilder(this.getPlayer(), item, s -> this.applyBackDataString(s, backData), key)
                                .persistentDataContainer("id", PersistentDataType.INTEGER, backData.getId())
                                .build();

                        menu.setItem(backSlotList.get(slot), itemStack);
                        slot++;
                    }
                    continue;
                }
                case "上一页" -> {
                    if (page <= 1) {
                        continue;
                    }
                }
                case "下一页" -> {
                    if (backList.size() <= maxEnd) {
                        continue;
                    }
                }
            }

            MenuUtil.setMenuItem(this.getPlayer(), menu, item, key);
        }

        return menu;
    }

    @Override
    public void open(InventoryOpenEvent event) {
        ActionUtil.runActionList(this.getPlayer(), getConfig().getStringList("openActions"));
    }

    @Override
    public void click(InventoryClickEvent event) {
        ItemStack itemStack = MenuUtil.getClickItem(event);
        if (itemStack == null) {
            return;
        }

        event.setCancelled(true);

        PersistentDataContainerView container = itemStack.getPersistentDataContainer();

        String key = container.get(new NamespacedKey(Main.instance, "key"), PersistentDataType.STRING);
        if (key == null) {
            return;
        }

        MenuUtil.runItemClickAction(this.getPlayer(), getConfig(), key);

        switch (key) {
            case "位置" -> {
                Integer id = container.get(new NamespacedKey(Main.instance, "id"), PersistentDataType.INTEGER);
                if (id == null) {
                    return;
                }

                MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(this.getPlayer());
                BackData backData = player.getBackData(id);

                Main.instance.getBungeeCordManager().teleportLocation(this.getPlayer(), backData.toBungeeCordLocation());
                Main.instance.getBungeeCordManager().sendMessage(this.getPlayer(), Main.instance.getConfigManager().getLangManager().i18n("commands.back.message"));
            }
            case "上一页" -> new BackMenu(this.getPlayer(), getPage() - 1).openMenu();
            case "下一页" -> new BackMenu(this.getPlayer(), getPage() + 1).openMenu();
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {
        ActionUtil.runActionList(getPlayer(), getConfig().getStringList("closeActions"));
    }

    /**
     * 处理位置数据实例文本
     *
     * @param message 文本
     * @param data    位置数据实例
     * @return 处理后的文本
     */
    private String applyBackDataString(String message, BackData data) {
        if (message == null) {
            return null;
        }

        Instant instant = Instant.ofEpochMilli(data.getTime());
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        return message
                .replace("{year}", String.valueOf(dateTime.getYear()))
                .replace("{month}", String.valueOf(dateTime.getMonthValue()))
                .replace("{day}", String.valueOf(dateTime.getDayOfMonth()))
                .replace("{hour}", String.valueOf(dateTime.getHour()))
                .replace("{minute}", String.valueOf(dateTime.getMinute()))
                .replace("{second}", String.valueOf(dateTime.getSecond()))
                .replace("{type}", Main.instance.getConfigManager().getLangManager().getString("commands.back.type." + data.getType()))
                .replace("{server}", data.getServer())
                .replace("{world}", data.getWorld())
                .replace("{x}", String.valueOf(data.getX()))
                .replace("{y}", String.valueOf(data.getY()))
                .replace("{z}", String.valueOf(data.getZ()))
                .replace("{yaw}", String.valueOf(data.getYaw()))
                .replace("{pitch}", String.valueOf(data.getPitch()));
    }
}
