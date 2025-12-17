package cn.chengzhimeow.mhdftools.bukkit.module.quitmessage.config;

import cn.chengzhimeow.cccondition.condition.ConditionBuilder;
import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.common.condition.ConditionManager;
import cn.chengzhimeow.mhdftools.bukkit.module.quitmessage.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

import java.util.List;

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
        if (list == null) return;

        for (String key : list.getKeys(false)) {
            ConfigurationSection section = list.getConfigurationSection(key);
            if (section == null) continue;

            List<ConditionBuilder.Builder> conditions = ConditionManager.getInstance().getConditionListFromConfig(section, "conditions");
            ConditionManager.getInstance().check(conditions);
        }
    }
}
