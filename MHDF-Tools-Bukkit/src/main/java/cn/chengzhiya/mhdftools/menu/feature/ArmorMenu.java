package cn.chengzhiya.mhdftools.menu.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.menu.AbstractMenu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.MenuConfigUtil;
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
public final class ArmorMenu extends AbstractMenu {
    private final YamlConfiguration config;
    private final Player target;

    public ArmorMenu(Player player, Player target) {
        super(
                List.of("invseeSettings.enable"),
                player
        );

        this.config = MenuConfigUtil.getMenuConfig("armor");
        this.target = target;
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory menu = MenuUtil.createInventory(this, getConfig());

        ConfigurationSection items = getConfig().getConfigurationSection("items");
        if (items == null) {
            return menu;
        }

        for (String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);
            if (item == null) {
                continue;
            }

            ItemStack armor = switch (key) {
                case "头盔" -> getPlayer().getInventory().getHelmet();
                case "胸甲" -> getPlayer().getInventory().getChestplate();
                case "裤子" -> getPlayer().getInventory().getLeggings();
                case "鞋子" -> getPlayer().getInventory().getBoots();
                default -> null;
            };

            if (armor != null) {
                MenuUtil.setMenuItem(menu, item, armor);
                continue;
            }

            MenuUtil.setMenuItem(getPlayer(), menu, item, key);
        }

        return menu;
    }

    @Override
    public void open(InventoryOpenEvent event) {
        ActionUtil.runActionList(getPlayer(), getConfig().getStringList("openActions"));
    }

    @Override
    public void click(InventoryClickEvent event) {
        ItemStack itemStack = MenuUtil.getClickItem(event);
        if (itemStack == null) {
            return;
        }

        PersistentDataContainerView container = itemStack.getPersistentDataContainer();
        String key = container.get(new NamespacedKey(Main.instance, "key"), PersistentDataType.STRING);
        if (key != null) {
            event.setCancelled(true);
            MenuUtil.runItemClickAction(getPlayer(), getConfig(), key);
            return;
        }

        this.updateArmor(event.getInventory());
    }

    @Override
    public void close(InventoryCloseEvent event) {
        ActionUtil.runActionList(getPlayer(), getConfig().getStringList("closeActions"));

        this.updateArmor(event.getInventory());
    }

    /**
     * 更新装备
     */
    private void updateArmor(Inventory menu) {
        List<String> armorList = List.of("头盔", "胸甲", "裤子", "鞋子");
        for (String key : armorList) {
            ConfigurationSection item = getConfig().getConfigurationSection("items." + key);
            if (item == null) {
                continue;
            }

            int slot = MenuUtil.getSlotList(item).get(0);
            ItemStack armor = menu.getItem(slot);

            switch (key) {
                case "头盔" -> getPlayer().getInventory().setHelmet(armor);
                case "胸甲" -> getPlayer().getInventory().setChestplate(armor);
                case "裤子" -> getPlayer().getInventory().setLeggings(armor);
                case "鞋子" -> getPlayer().getInventory().setBoots(armor);
            }
        }
    }
}
