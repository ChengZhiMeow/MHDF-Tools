package cn.chengzhimeow.mhdftools.bukkit.menu.feature.chat;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.ccyaml.configuration.yaml.YamlConfiguration;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.builder.ItemStackBuilder;
import cn.chengzhimeow.mhdftools.bukkit.config.folder.MenuManager;
import cn.chengzhimeow.mhdftools.bukkit.menu.Menu;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.menu.MenuUtil;
import io.papermc.paper.persistence.PersistentDataContainerView;
import lombok.Getter;
import org.bukkit.NamespacedKey;
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
        this.config = MenuManager.getSettingInstance().getData("lookItem.yml");
        this.data = data;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory menu = MenuUtil.createInventory(this, this.getConfig());

        ConfigurationSection items = this.getConfig().getConfigurationSection("items");
        if (items == null) return menu;

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
        if (itemStack == null) return;


        event.setCancelled(true);

        PersistentDataContainerView container = itemStack.getPersistentDataContainer();

        String key = container.get(new NamespacedKey(Main.instance, "key"), PersistentDataType.STRING);
        if (key == null) return;

        MenuUtil.runItemClickAction(super.getPlayer(), this.getConfig(), key);
    }

    @Override
    public void close(InventoryCloseEvent event) {
        ActionUtil.runActionList(super.getPlayer(), this.getConfig().getStringList("closeActions"));
    }
}
