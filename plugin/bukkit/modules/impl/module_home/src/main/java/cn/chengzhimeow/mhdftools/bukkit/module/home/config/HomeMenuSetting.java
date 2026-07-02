package cn.chengzhimeow.mhdftools.bukkit.module.home.config;

import cn.chengzhimeow.cccondition.condition.ConditionBuilder;
import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.api.entity.BuilderItem;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionAction;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionActionManager;
import cn.chengzhimeow.mhdftools.bukkit.common.condition.ConditionManager;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.item.MenuActionType;
import cn.chengzhimeow.mhdftools.bukkit.module.home.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class HomeMenuSetting extends AbstractYamlSetting<HomeMenuSetting.Config> {
    @Getter(lazy = true)
    private static final HomeMenuSetting instance = new HomeMenuSetting();
    @Getter private Config config;

    private HomeMenuSetting() {
    }

    @Override
    public String originFilePath() {
        return "module/" + ModuleMain.instance.getId() + "/menu/home.yml";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }

    @Override
    public void reload() {
        super.reload();

        Map<String, Config.MenuItem> items = new LinkedHashMap<>();
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
                new Config.Keys(
                        Objects.requireNonNull(super.getData().getString("keys.home", "H"))
                ),
                items
        );
    }

    private Config.MenuItem toMenuItem(ConfigurationSection section, String id) {
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

        return new Config.MenuItem(
                id,
                Objects.requireNonNull(section.getString("by", "Vanilla")),
                Objects.requireNonNull(section.getString("type", "paper")),
                section.getString("name"),
                section.getStringList("lore"),
                section.getInt("custom_model_data", null),
                section.getInt("amount"),
                ConditionManager.getInstance().getConditionListFromConfig(section, "conditions"),
                actions
        );
    }

    public record Config(
            TextComponent title,
            List<String> slots,
            Keys keys,
            Map<String, MenuItem> items
    ) {
        public record Keys(
                String home
        ) {
        }

        public record MenuItem(
                String id,
                String by,
                String type,
                String name,
                List<String> lore,
                Integer customModelData,
                int amount,
                List<ConditionBuilder.Builder> conditions,
                Map<MenuActionType, List<ConditionAction>> actions
        ) {
            public BuilderItem toBuilderItem(Map<String, String> placeholders, Map<String, String> pdc) {
                return new BuilderItem(
                        this.id,
                        this.by,
                        this.type,
                        this.name == null ? null : ColorUtil.color(this.replace(this.name, placeholders)),
                        this.lore.stream()
                                .map(line -> this.replace(line, placeholders))
                                .map(ColorUtil::color)
                                .map(component -> (Component) component)
                                .toList(),
                        this.customModelData,
                        this.amount,
                        pdc
                );
            }

            public boolean checkConditions(Player player, @NotNull Map<String, Object> params) {
                return ConditionManager.getInstance().condition(player, this.conditions, params);
            }

            public void action(Player player, ClickType clickType, @NotNull Map<String, Object> params) {
                this.actions.forEach((type, actions) -> {
                    if (!type.matches(clickType)) return;
                    for (ConditionAction action : actions) {
                        ConditionActionManager.getInstance().actionWithCondition(player, action, params);
                    }
                });
            }

            private String replace(String text, Map<String, String> placeholders) {
                String result = text;
                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                    result = result.replace("{" + entry.getKey() + "}", entry.getValue());
                }
                return result;
            }
        }
    }
}
