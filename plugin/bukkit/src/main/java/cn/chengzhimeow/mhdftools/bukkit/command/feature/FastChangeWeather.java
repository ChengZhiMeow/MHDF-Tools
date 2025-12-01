package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.FastChangeWeatherUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Getter
final class FastChangeWeather extends Command {
    private final ConcurrentHashMap<String, ConfigurationSection> commandConfigHashMap = new ConcurrentHashMap<>();

    public FastChangeWeather() {
        super(
                null,
                List.of("fastChangeWeatherSettings.enable"),
                "快速调节天气",
                "mhdftools.commands.fastchangeweather",
                false,
                FastChangeWeatherUtil.getCommandList().toArray(new String[0])
        );

        {
            ConfigurationSection config = ConfigSetting.getInstance().getData().getConfigurationSection("fastChangeWeatherSettings.weather");
            if (config == null) {
                return;
            }

            for (String key : config.getKeys(false)) {
                ConfigurationSection weather = config.getConfigurationSection(key);
                if (weather == null) {
                    continue;
                }

                for (String command : weather.getStringList("commands")) {
                    this.getCommandConfigHashMap().put(command, weather);
                }
            }
        }
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        boolean storm = this.getCommandConfigHashMap().get(label) != null &&
                this.getCommandConfigHashMap().get(label).getBoolean("storm");
        boolean thunder = this.getCommandConfigHashMap().get(label) != null &&
                this.getCommandConfigHashMap().get(label).getBoolean("thunder");

        for (World world : Bukkit.getWorlds()) {
            world.setStorm(storm);
            world.setThundering(thunder);
        }

        sender.sendMessage(LangSetting.getInstance().i18n("commands.fastchangeweather.message")
                .replace("{storm}", storm ? LangSetting.getInstance().i18n("enable") : LangSetting.getInstance().i18n("disable"))
                .replace("{thunder}", thunder ? LangSetting.getInstance().i18n("enable") : LangSetting.getInstance().i18n("disable"))
        );
    }
}
