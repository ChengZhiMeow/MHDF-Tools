package cn.chengzhimeow.mhdftools.bukkit.module.fastuse.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionAction;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionActionManager;
import cn.chengzhimeow.mhdftools.bukkit.module.fastuse.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

public final class ConfigSetting extends AbstractYamlSetting {
    @Getter(lazy = true)
    private static final ConfigSetting instance = new ConfigSetting();

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

        // 检查条件与操作配置是否正确
        ConfigurationSection list = this.getData().getConfigurationSection("list");
        for (String key : Objects.requireNonNull(list).getKeys(false)) {
            ConfigurationSection section = list.getConfigurationSection(key);
            List<ConditionAction> conditionActions = ConditionActionManager.getInstance().getConditionActionListFromConfig(Objects.requireNonNull(section), "action");
            ConditionActionManager.getInstance().check(conditionActions);
        }
    }
}
