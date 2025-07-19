package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.Command;
import cn.chengzhiya.mhdftools.util.feature.FastChangeTimeUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Getter
final class FastChangeTime extends Command {
    private final ConcurrentHashMap<String, ConfigurationSection> commandConfigHashMap = new ConcurrentHashMap<>();

    public FastChangeTime() {
        super(
                List.of("fastChangeTimeSettings.enable"),
                "快速调节时间",
                "mhdftools.commands.fastchangetime",
                false,
                FastChangeTimeUtil.getCommandList().toArray(new String[0])
        );

        {
            ConfigurationSection config = Main.instance.getConfigManager().getConfigManager().getData().getConfigurationSection("fastChangeTimeSettings.time");
            if (config == null) {
                return;
            }

            for (String key : config.getKeys(false)) {
                ConfigurationSection time = config.getConfigurationSection(key);
                if (time == null) {
                    continue;
                }

                for (String command : time.getStringList("commands")) {
                    getCommandConfigHashMap().put(command, time);
                }
            }
        }
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        int time = getCommandConfigHashMap().get(label) != null
                ? getCommandConfigHashMap().get(label).getInt("time") : 0;

        for (World world : Bukkit.getWorlds()) {
            world.setTime(time);
        }

        sender.sendMessage(Main.instance.getConfigManager().getLangManager().i18n("commands.fastchangetime.message")
                .replace("{time}", String.valueOf(time))
        );
    }
}
