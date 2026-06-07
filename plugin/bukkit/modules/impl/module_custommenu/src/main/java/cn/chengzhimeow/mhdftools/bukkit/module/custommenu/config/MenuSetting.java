package cn.chengzhimeow.mhdftools.bukkit.module.custommenu.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.ccyaml.configuration.yaml.YamlConfiguration;
import cn.chengzhimeow.ccyaml.manager.AbstractFolderYamlManager;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionAction;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionActionManager;
import cn.chengzhimeow.mhdftools.bukkit.common.condition.ConditionManager;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.item.MenuActionType;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.item.MenuItem;
import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.ModuleMain;
import cn.chengzhimeow.mhdftools.config.ConfigManager;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public final class MenuSetting extends AbstractFolderYamlManager {
    @Getter(lazy = true)
    private static final MenuSetting instance = new MenuSetting();
    private final Map<String, Config.Menu> menus = new LinkedHashMap<>();
    private final Map<String, Config.Menu> commandMenus = new HashMap<>();
    @Getter private Config config = new Config(Map.of());

    private MenuSetting() {
        super(ConfigManager.getInstance().getYamlManager());
    }

    @Override
    public void saveDefaultFile() {
        if (super.getFolder().exists()) return;
        super.saveDefaultFile();
    }

    public void update() {
    }

    @Override
    public String originFilePath() {
        return "module/" + ModuleMain.instance.getId() + "/menu";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }

    @Override
    public void reload() {
        super.reload();

        this.menus.clear();
        this.commandMenus.clear();

        for (File file : super.getFileList()) {
            if (!file.getName().endsWith(".yml")) continue;

            YamlConfiguration data = super.getData(file);
            if (data == null) continue;

            String id = file.getName().substring(0, file.getName().length() - ".yml".length());
            Config.Menu menu = this.toMenu(id, data);
            this.menus.put(id, menu);
            for (String command : menu.commands()) {
                this.commandMenus.put(command.toLowerCase(), menu);
            }
        }

        this.config = new Config(Map.copyOf(this.menus));
    }

    public Config.Menu getMenu(String id) {
        return this.menus.get(id);
    }

    public Config.Menu getMenuByCommand(String command) {
        return this.commandMenus.get(command.toLowerCase());
    }

    public Set<String> getMenuIds() {
        return this.menus.keySet();
    }

    private @NotNull Config.Menu toMenu(@NotNull String id, @NotNull YamlConfiguration data) {
        Map<String, MenuItem> items = new HashMap<>();
        ConfigurationSection itemsSection = data.getConfigurationSection("items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection == null) continue;
                items.put(key, this.toMenuItem(itemSection, key));
            }
        }

        List<String> slots = data.getStringList("slots");
        if (slots.isEmpty()) {
            int size = Math.max(data.getInt("size"), 9);
            int rows = Math.max(1, Math.min(6, (int) Math.ceil(size / 9.0)));
            slots = new ArrayList<>();
            for (int i = 0; i < rows; i++) slots.add("         ");
        }

        return new Config.Menu(
                id,
                ColorUtil.color(data.getString("title", "")),
                slots,
                data.getStringList("commands"),
                ConditionActionManager.getInstance().getConditionActionListFromConfig(data, "open_actions"),
                ConditionActionManager.getInstance().getConditionActionListFromConfig(data, "close_actions"),
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
            Map<String, Menu> menus
    ) {
        public record Menu(
                String id,
                TextComponent title,
                List<String> slots,
                List<String> commands,
                List<ConditionAction> openActions,
                List<ConditionAction> closeActions,
                Map<String, MenuItem> items
        ) {
        }
    }
}
