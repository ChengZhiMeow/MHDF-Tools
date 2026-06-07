package cn.chengzhimeow.mhdftools.bukkit.module.invsee.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionAction;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionActionManager;
import cn.chengzhimeow.mhdftools.bukkit.common.condition.ConditionManager;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.item.MenuActionType;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.item.MenuItem;
import cn.chengzhimeow.mhdftools.bukkit.module.invsee.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class ArmorMenuSetting extends AbstractYamlSetting<ArmorMenuSetting.Config> {
    @Getter(lazy = true)
    private static final ArmorMenuSetting instance = new ArmorMenuSetting();
    @Getter private Config config;

    private ArmorMenuSetting() {
    }

    @Override
    public String originFilePath() {
        return "module/" + ModuleMain.instance.getId() + "/menu/armor.yml";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }

    @Override
    public void reload() {
        super.reload();

        Map<String, MenuItem> items = new HashMap<>();
        ConfigurationSection itemsSection = super.getData().getConfigurationSection("items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection == null) continue;
                items.put(key, this.toMenuItem(itemSection, key));
            }
        }

        this.config = new Config(
                ColorUtil.color(super.getData().getString("title", "")),
                super.getData().getStringList("slots"),
                ConditionActionManager.getInstance().getConditionActionListFromConfig(super.getData(), "open_actions"),
                ConditionActionManager.getInstance().getConditionActionListFromConfig(super.getData(), "close_actions"),
                new Config.EquipmentSlots(
                        super.getData().getString("equipment_slots.helmet", "H"),
                        super.getData().getString("equipment_slots.chestplate", "C"),
                        super.getData().getString("equipment_slots.leggings", "L"),
                        super.getData().getString("equipment_slots.boots", "B")
                ),
                items
        );
    }

    private @NotNull MenuItem toMenuItem(@NotNull ConfigurationSection section, @NotNull String id) {
        Map<MenuActionType, List<ConditionAction>> actions = new EnumMap<>(MenuActionType.class);
        ConfigurationSection actionsSection = section.getConfigurationSection("actions");
        if (actionsSection != null) {
            for (String key : actionsSection.getKeys(false)) {
                actions.put(
                        MenuActionType.valueOf(key.toUpperCase()),
                        ConditionActionManager.getInstance().getConditionActionListFromConfig(actionsSection, key)
                );
            }
        }

        Map<String, String> pdc = new HashMap<>();
        ConfigurationSection pdcSection = section.getConfigurationSection("pdc");
        if (pdcSection != null) {
            for (String key : pdcSection.getKeys(false)) {
                pdc.put(key, pdcSection.getString(key, ""));
            }
        }

        List<Component> lore = section.getStringList("lore").stream()
                .map(ColorUtil::color)
                .map(component -> (Component) component)
                .toList();

        return new MenuItem(
                id,
                section.getString("slot"),
                Objects.requireNonNull(section.getString("by", "Vanilla")),
                Objects.requireNonNull(section.getString("type", "paper")),
                section.getString("name") == null ? null : ColorUtil.color(section.getString("name")),
                lore,
                section.getInt("custom_model_data", null),
                section.getInt("amount"),
                pdc,
                ConditionManager.getInstance().getConditionListFromConfig(section, "conditions"),
                actions
        );
    }

    public record Config(
            TextComponent title,
            List<String> slots,
            List<ConditionAction> openActions,
            List<ConditionAction> closeActions,
            EquipmentSlots equipmentSlots,
            Map<String, MenuItem> items
    ) {
        public record EquipmentSlots(
                String helmet,
                String chestplate,
                String leggings,
                String boots
        ) {
            public boolean contains(String key) {
                return this.helmet.equals(key)
                        || this.chestplate.equals(key)
                        || this.leggings.equals(key)
                        || this.boots.equals(key);
            }
        }
    }
}
