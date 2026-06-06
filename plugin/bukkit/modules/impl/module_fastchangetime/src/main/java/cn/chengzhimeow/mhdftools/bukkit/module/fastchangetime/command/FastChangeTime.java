package cn.chengzhimeow.mhdftools.bukkit.module.fastchangetime.command;

import cn.chengzhimeow.mhdftools.bukkit.module.fastchangetime.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.fastchangetime.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.fastchangetime.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

final class FastChangeTime extends Command {
    public FastChangeTime() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "快速调节时间",
                "mhdftools.commands.fastchangetime",
                false,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        ConfigSetting.Config.Time time = ConfigSetting.getInstance().getConfig().commandTimeMap().get(label);
        int value = time == null ? 0 : time.time();

        for (World world : Bukkit.getWorlds()) {
            world.setTime(value);
        }

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().fastChangeTime().message()
                .replace("{time}", String.valueOf(value)));
    }
}
