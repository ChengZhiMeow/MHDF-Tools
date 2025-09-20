package cn.chengzhimeow.mhdftools.bukkit.menu.feature;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.ccyaml.configuration.yaml.YamlConfiguration;
import cn.chengzhimeow.mhdftools.bukkit.Main;
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
public final class ArmorMenu extends Menu {
    private final YamlConfiguration config;
    private final Player target;

    public ArmorMenu(Player player, Player target) {
        super(
                List.of("invseeSettings.enable"),
                player
        );

        this.config = MenuManager.getSettingInstance().getData("armor.yml");
        this.target = target;
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

            ItemStack armor = switch (key) {
                case "头盔" -> super.getPlayer().getInventory().getHelmet();
                case "胸甲" -> super.getPlayer().getInventory().getChestplate();
                case "裤子" -> super.getPlayer().getInventory().getLeggings();
                case "鞋子" -> super.getPlayer().getInventory().getBoots();
                default -> null;
            };

            if (armor != null) {
                MenuUtil.setMenuItem(menu, item, armor);
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

        PersistentDataContainerView container = itemStack.getPersistentDataContainer();
        String key = container.get(new NamespacedKey(Main.instance, "key"), PersistentDataType.STRING);
        if (key != null) {
            event.setCancelled(true);
            MenuUtil.runItemClickAction(super.getPlayer(), this.getConfig(), key);
            return;
        }

        this.updateArmor(event.getInventory());
    }

    @Override
    public void close(InventoryCloseEvent event) {
        ActionUtil.runActionList(super.getPlayer(), this.getConfig().getStringList("closeActions"));

        this.updateArmor(event.getInventory());
    }

    /**
     * 更新装备
     */
    private void updateArmor(Inventory menu) {
        List<String> armorList = List.of("头盔", "胸甲", "裤子", "鞋子");
        for (String key : armorList) {
            ConfigurationSection item = this.getConfig().getConfigurationSection("items." + key);
            if (item == null) {
                continue;
            }

            int slot = MenuUtil.getSlotList(item).get(0);
            ItemStack armor = menu.getItem(slot);

            switch (key) {
                case "头盔" -> super.getPlayer().getInventory().setHelmet(armor);
                case "胸甲" -> super.getPlayer().getInventory().setChestplate(armor);
                case "裤子" -> super.getPlayer().getInventory().setLeggings(armor);
                case "鞋子" -> super.getPlayer().getInventory().setBoots(armor);
            }
        }
    }
}
