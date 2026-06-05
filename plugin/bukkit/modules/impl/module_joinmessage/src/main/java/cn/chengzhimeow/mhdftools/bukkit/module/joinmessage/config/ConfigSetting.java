package cn.chengzhimeow.mhdftools.bukkit.module.joinmessage.config;

import cn.chengzhimeow.cccondition.condition.ConditionBuilder;
import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.common.condition.ConditionManager;
import cn.chengzhimeow.mhdftools.bukkit.module.joinmessage.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        Map<String, Config.Group> groups = new LinkedHashMap<>();
        ConfigurationSection groupSection = super.getData().getConfigurationSection("groups");
        if (groupSection != null) {
            for (String key : groupSection.getKeys(false)) {
                ConfigurationSection section = groupSection.getConfigurationSection(key);
                if (section == null) continue;

                List<ConditionBuilder.Builder> conditions = ConditionManager.getInstance().getConditionListFromConfig(section, "conditions");
                ConditionManager.getInstance().check(conditions);

                groups.put(key, new Config.Group(
                        section.getInt("weight"),
                        section.getString("message"),
                        conditions
                ));
            }
        }

        this.config = new Config(
                super.getData().getBoolean("enable"),
                super.getData().getBoolean("remove_message"),
                groups
        );
    }

    public record Config(
            boolean enable,
            boolean removeMessage,
            Map<String, Group> groups
    ) {
        public record Group(
                int weight,
                String message,
                List<ConditionBuilder.Builder> conditions
        ) {
        }
    }
}
