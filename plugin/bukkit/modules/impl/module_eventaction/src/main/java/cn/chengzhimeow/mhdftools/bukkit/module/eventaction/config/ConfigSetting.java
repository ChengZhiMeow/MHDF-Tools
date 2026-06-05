package cn.chengzhimeow.mhdftools.bukkit.module.eventaction.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionAction;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionActionManager;
import cn.chengzhimeow.mhdftools.bukkit.module.eventaction.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

        List<Config.Action> actions = new ArrayList<>();
        ConfigurationSection list = super.getData().getConfigurationSection("list");
        if (list != null) {
            for (String key : list.getKeys(false)) {
                ConfigurationSection section = list.getConfigurationSection(key);
                if (section == null) continue;

                List<ConditionAction> conditionActions = ConditionActionManager.getInstance().getConditionActionListFromConfig(section, "action");
                ConditionActionManager.getInstance().check(conditionActions);

                actions.add(new Config.Action(
                        key,
                        section.getString("event"),
                        conditionActions
                ));
            }
        }

        this.config = new Config(
                super.getData().getBoolean("enable"),
                actions
        );
    }

    public record Config(
            boolean enable,
            List<Action> actions
    ) {
        public record Action(
                String id,
                String event,
                List<ConditionAction> actions
        ) {
        }
    }
}
