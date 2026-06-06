package cn.chengzhimeow.mhdftools.bukkit.menu.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionAction;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionActionManager;
import cn.chengzhimeow.mhdftools.bukkit.common.condition.ConditionManager;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.item.MenuActionType;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.item.MenuItem;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MenuConfigUtil {
    public static @Nullable MenuItem getMenuItem(
            @NotNull ConfigurationSection section,
            @NotNull String key
    ) {
        return getMenuItem(section, key, key);
    }

    public static @Nullable MenuItem getMenuItem(
            @NotNull ConfigurationSection section,
            @NotNull String key,
            @NotNull String id
    ) {
        ConfigurationSection itemSection = section.getConfigurationSection(key);
        if (itemSection == null) return null;
        return toMenuItem(itemSection, id);
    }

    public static @NotNull MenuItem toMenuItem(
            @NotNull ConfigurationSection section,
            @NotNull String id
    ) {
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
                section.getString("by", "Vanilla"),
                section.getString("type", "air"),
                section.getString("name") == null ? null : ColorUtil.color(section.getString("name")),
                lore,
                section.getInt("custom_model_data", null),
                section.getInt("amount", 1),
                pdc,
                ConditionManager.getInstance().getConditionListFromConfig(section, "conditions"),
                actions
        );
    }

    private MenuConfigUtil() {
    }
}
