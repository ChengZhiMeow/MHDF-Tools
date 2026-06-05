package cn.chengzhimeow.mhdftools.bukkit.module.fastuse.config;

import cn.chengzhimeow.mhdftools.bukkit.module.fastuse.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

public final class ConfigSetting extends AbstractYamlSetting<ConfigSetting.Config> {
    @Getter(lazy = true)
    private static final ConfigSetting instance = new ConfigSetting();
    @Getter private Config config;

    private ConfigSetting() {
    }

    @Override
    public String originFilePath() {
        return "module/" + ModuleMain.instance.getId() + "/config.yml";
    }

    @Override
    public String filePath() {
        return this.originFilePath();
    }

    @Override
    public void reload() {
        super.reload();

        this.config = new Config(
                super.getData().getBoolean("enable"),
                new Config.Items(
                        super.getData().getBoolean("items.shulker_box"),
                        super.getData().getBoolean("items.ender_chest"),
                        super.getData().getBoolean("items.crafting_table")
                )
        );
    }

    public record Config(
            boolean enable,
            Items items
    ) {
        public record Items(
                boolean shulkerBox,
                boolean enderChest,
                boolean craftingTable
        ) {
        }
    }
}
