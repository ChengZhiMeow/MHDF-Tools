package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.command.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.FastChangeTimeUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
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
            ConfigurationSection config = ConfigSetting.getSettingInstance().getData().getConfigurationSection("fastChangeTimeSettings.time");
            if (config == null) {
                return;
            }

            for (String key : config.getKeys(false)) {
                ConfigurationSection time = config.getConfigurationSection(key);
                if (time == null) {
                    continue;
                }

                for (String command : time.getStringList("commands")) {
                    this.getCommandConfigHashMap().put(command, time);
                }
            }
        }
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        int time = this.getCommandConfigHashMap().get(label) != null
                ? this.getCommandConfigHashMap().get(label).getInt("time") : 0;

        for (World world : Bukkit.getWorlds()) {
            world.setTime(time);
        }

        sender.sendMessage(LangSetting.getSettingInstance().i18n("commands.fastchangetime.message")
                .replace("{time}", String.valueOf(time))
        );
    }
}
