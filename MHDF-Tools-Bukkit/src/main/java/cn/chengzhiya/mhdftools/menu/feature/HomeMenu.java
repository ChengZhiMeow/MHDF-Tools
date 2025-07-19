package cn.chengzhiya.mhdftools.menu.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.HomeData;
import cn.chengzhiya.mhdftools.menu.Menu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
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

@Getter
public final class HomeMenu extends Menu {
    private final YamlConfiguration config;
    private final int page;

    public HomeMenu(Player player, int page) {
        super(
                List.of("homeSettings.enable"),
                player
        );

        this.config = Main.instance.getConfigManager().getMenuManager().getData("home.yml");
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory menu = MenuUtil.createInventory(this, this.getConfig());

        ConfigurationSection items = this.getConfig().getConfigurationSection("items");
        if (items == null) {
            return menu;
        }

        MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(super.getPlayer());
        List<HomeData> homeList = player.getHomeList();
        List<Integer> homeSlotList = MenuUtil.getSlotList(items.getConfigurationSection("家"));

        int start = (this.getPage() - 1) * homeSlotList.size();
        int maxEnd = this.getPage() * homeSlotList.size();
        int end = Math.min(homeList.size(), maxEnd);

        for (String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);
            if (item == null) {
                continue;
            }

            switch (key) {
                case "家" -> {
                    int slot = 0;
                    for (int i = start; i < end; i++) {
                        HomeData homeData = homeList.get(i);

                        ItemStack itemStack = MenuUtil.getMenuItemStackBuilder(super.getPlayer(), item, s -> this.applyHomeDataString(s, homeData), key)
                                .persistentDataContainer("home", PersistentDataType.STRING, homeData.getHome())
                                .build();

                        menu.setItem(homeSlotList.get(slot), itemStack);
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
                    if (homeList.size() <= maxEnd) {
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
        if (itemStack == null) {
            return;
        }

        event.setCancelled(true);

        PersistentDataContainerView container = itemStack.getPersistentDataContainer();

        String key = container.get(new NamespacedKey(Main.instance, "key"), PersistentDataType.STRING);
        if (key == null) {
            return;
        }

        MenuUtil.runItemClickAction(super.getPlayer(), this.getConfig(), key);

        switch (key) {
            case "家" -> {
                String home = container.get(new NamespacedKey(Main.instance, "home"), PersistentDataType.STRING);
                if (home == null) {
                    return;
                }

                MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(super.getPlayer());
                HomeData homeData = player.getHome(home);

                Main.instance.getBungeeCordManager().teleportLocation(super.getPlayer(), homeData.toBungeeCordLocation());
                Main.instance.getBungeeCordManager().sendMessage(super.getPlayer(), Main.instance.getConfigManager().getLangManager().i18n("commands.home.teleportMessage")
                        .replace("{home}", homeData.getHome())
                );
            }
            case "上一页" -> new HomeMenu(super.getPlayer(), this.getPage() - 1).openMenu();
            case "下一页" -> new HomeMenu(super.getPlayer(), this.getPage() + 1).openMenu();
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
     * @param data    家数据实例
     * @return 处理后的文本
     */
    private String applyHomeDataString(String message, HomeData data) {
        if (message == null) {
            return null;
        }

        return message
                .replace("{name}", data.getHome())
                .replace("{server}", data.getServer())
                .replace("{world}", data.getWorld())
                .replace("{x}", String.valueOf(data.getX()))
                .replace("{y}", String.valueOf(data.getY()))
                .replace("{z}", String.valueOf(data.getZ()))
                .replace("{yaw}", String.valueOf(data.getYaw()))
                .replace("{pitch}", String.valueOf(data.getPitch()));
    }
}
