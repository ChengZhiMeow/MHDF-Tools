package cn.chengzhimeow.mhdftools.bukkit.util.feature;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;

import java.util.ArrayList;
import java.util.List;

public final class FastChangeWeatherUtil {
    /**
     * 获取命令列表
     *
     * @return 命令列表
     */
    public static List<String> getCommandList() {
        ConfigurationSection config = ConfigSetting.getSettingInstance().getData().getConfigurationSection("fastChangeWeatherSettings.weather");
        if (config == null) return new ArrayList<>();

        List<String> commandList = new ArrayList<>();
        for (String key : config.getKeys(false)) {
            ConfigurationSection time = config.getConfigurationSection(key);
            if (time == null) continue;

            commandList.addAll(time.getStringList("commands"));
        }

        return commandList;
    }
}
