package cn.chengzhimeow.mhdftools.bukkit.module.fastchangeweather.command;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.module.fastchangeweather.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.fastchangeweather.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.fastchangeweather.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

final class FastChangeWeather extends Command {
    public FastChangeWeather() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                LangSetting.getInstance().getConfig().commands().fastChangeWeather().description(),
                "mhdftools.commands.fastchangeweather",
                false,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        ConfigSetting.Config.Weather weather = ConfigSetting.getInstance().getConfig().commandWeatherMap().get(label);
        boolean storm = weather != null && weather.storm();
        boolean thunder = weather != null && weather.thunder();

        CCScheduler.getInstance().getGlobalRegionScheduler().runTask(MHDFToolsBukkit.getInstance(), () -> {
            for (World world : Bukkit.getWorlds()) {
                world.setStorm(storm);
                world.setThundering(thunder);
            }
        });

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().fastChangeWeather().message()
                .replace("{storm}", storm
                                    ? GlobalLangSetting.getInstance().getConfig().enable()
                                    : GlobalLangSetting.getInstance().getConfig().disable())
                .replace("{thunder}", thunder
                                      ? GlobalLangSetting.getInstance().getConfig().enable()
                                      : GlobalLangSetting.getInstance().getConfig().disable()));
    }
}
