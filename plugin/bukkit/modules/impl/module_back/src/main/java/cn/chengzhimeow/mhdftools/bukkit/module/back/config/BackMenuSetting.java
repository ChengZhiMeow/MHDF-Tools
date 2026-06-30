package cn.chengzhimeow.mhdftools.bukkit.module.back.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.api.entity.BuilderItem;
import cn.chengzhimeow.mhdftools.bukkit.module.back.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class BackMenuSetting extends AbstractYamlSetting<BackMenuSetting.Config> {
    @Getter(lazy = true)
    private static final BackMenuSetting instance = new BackMenuSetting();
    @Getter private Config config;

    private BackMenuSetting() {
    }

    @Override
    public String originFilePath() {
        return "module/" + ModuleMain.instance.getId() + "/menu/back.yml";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }

    @Override
    public void reload() {
        super.reload();

        Map<String, Config.MenuItem> items = new HashMap<>();
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
                        Objects.requireNonNull(super.getData().getString("keys.location", "L")),
                        Objects.requireNonNull(super.getData().getString("keys.previous_page", "P")),
                        Objects.requireNonNull(super.getData().getString("keys.next_page", "N"))
                ),
                items
        );
    }

    private Config.MenuItem toMenuItem(ConfigurationSection section, String id) {
        return new Config.MenuItem(
                id,
                Objects.requireNonNull(section.getString("by", "Vanilla")),
                Objects.requireNonNull(section.getString("type", "paper")),
                section.getString("name"),
                section.getStringList("lore"),
                section.getInt("custom_model_data", null),
                section.getInt("amount")
        );
    }

    public record Config(
            TextComponent title,
            List<String> slots,
            Keys keys,
            Map<String, MenuItem> items
    ) {
        public record Keys(
                String location,
                String previousPage,
                String nextPage
        ) {
        }

        public record MenuItem(
                String id,
                String by,
                String type,
                String name,
                List<String> lore,
                Integer customModelData,
                int amount
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
