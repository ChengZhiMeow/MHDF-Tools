package cn.chengzhimeow.mhdftools.bukkit.module.fastchangeweather.config;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.module.fastchangeweather.ModuleMain;
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

        List<Config.Weather> weatherList = new ArrayList<>();
        Map<String, Config.Weather> commandWeatherMap = new LinkedHashMap<>();
        ConfigurationSection weatherSection = super.getData().getConfigurationSection("weather");
        if (weatherSection != null) {
            for (String key : weatherSection.getKeys(false)) {
                ConfigurationSection section = weatherSection.getConfigurationSection(key);
                if (section == null) continue;

                Config.Weather weather = new Config.Weather(
                        key,
                        section.getBoolean("storm"),
                        section.getBoolean("thunder"),
                        section.getStringList("commands")
                );
                weatherList.add(weather);
                weather.commands().forEach(command -> commandWeatherMap.put(command, weather));
            }
        }

        this.config = new Config(
                super.getData().getBoolean("enable"),
                weatherList,
                commandWeatherMap
        );
    }

    public record Config(
            boolean enable,
            List<Weather> weatherList,
            Map<String, Weather> commandWeatherMap
    ) {
        public List<String> commands() {
            return new ArrayList<>(this.commandWeatherMap.keySet());
        }

        public record Weather(
                String id,
                boolean storm,
                boolean thunder,
                List<String> commands
        ) {
        }
    }
}
