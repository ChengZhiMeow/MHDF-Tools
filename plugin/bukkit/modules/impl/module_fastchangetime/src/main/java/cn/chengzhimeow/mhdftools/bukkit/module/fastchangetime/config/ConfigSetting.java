package cn.chengzhimeow.mhdftools.bukkit.module.fastchangetime.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.module.fastchangetime.ModuleMain;
import cn.chengzhimeow.mhdftools.config.AbstractYamlSetting;
import lombok.Getter;

import java.util.ArrayList;
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

        List<Config.Time> times = new ArrayList<>();
        Map<String, Config.Time> commandTimeMap = new LinkedHashMap<>();
        ConfigurationSection timeSection = super.getData().getConfigurationSection("time");
        if (timeSection != null) {
            for (String key : timeSection.getKeys(false)) {
                ConfigurationSection section = timeSection.getConfigurationSection(key);
                if (section == null) continue;

                Config.Time time = new Config.Time(
                        key,
                        section.getInt("time"),
                        section.getStringList("commands")
                );
                times.add(time);
                time.commands().forEach(command -> commandTimeMap.put(command, time));
            }
        }

        this.config = new Config(
                super.getData().getBoolean("enable"),
                times,
                commandTimeMap
        );
    }

    public record Config(
            boolean enable,
            List<Time> times,
            Map<String, Time> commandTimeMap
    ) {
        public List<String> commands() {
            return new ArrayList<>(this.commandTimeMap.keySet());
        }

        public record Time(
                String id,
                int time,
                List<String> commands
        ) {
        }
    }
}
