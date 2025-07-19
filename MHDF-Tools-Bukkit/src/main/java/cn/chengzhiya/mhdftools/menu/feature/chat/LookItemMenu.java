package cn.chengzhiya.mhdftools.menu.feature.chat;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.builder.ItemStackBuilder;
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
public final class LookItemMenu extends Menu {
    private final YamlConfiguration config;
    private final byte[] data;

    public LookItemMenu(Player player, byte[] data) {
        super(
                List.of("chatSettings.enable", "chatSettings.showItem.enable"),
                player
        );
        this.config = Main.instance.getConfigManager().getMenuManager().getData("lookItem.yml");
        this.data = data;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory menu = MenuUtil.createInventory(this, this.getConfig());

        ConfigurationSection items = this.getConfig().getConfigurationSection("items");
        if (items == null) {
            return menu;
        }

        for (String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);
            if (item == null) {
                continue;
            }

            if (key.equals("物品")) {
                ItemStack itemStack = new ItemStackBuilder(super.getPlayer(), ItemStack.deserializeBytes(this.getData()))
                        .persistentDataContainer("key", PersistentDataType.STRING, key)
                        .build();

                MenuUtil.setMenuItem(menu, item, itemStack);
                continue;
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
    }

    @Override
    public void close(InventoryCloseEvent event) {
        ActionUtil.runActionList(super.getPlayer(), this.getConfig().getStringList("closeActions"));
    }
}
