package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.command.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class FlyTime extends Command {
    public FlyTime() {
        super(
                List.of("flySettings.enable"),
                "限时飞行",
                "mhdftools.commands.flytime",
                false,
                ConfigSetting.getSettingInstance().getData().getStringList("flySettings.flytimeCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 3) {
            switch (args[0]) {
                case "set", "add", "take" -> {
                    MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(args[1]);
                    long inputTime;
                    try {
                        inputTime = Long.parseLong(args[2]);
                    } catch (NumberFormatException e) {
                        ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.flytime.timeFormatError"));
                        return;
                    }

                    if (args[0].equals("set")) {
                        mhdfPlayer.setFlyTime(inputTime);
                    }

                    if (args[0].equals("add")) {
                        mhdfPlayer.addFlyTime(inputTime);
                    }

                    if (args[0].equals("take")) {
                        mhdfPlayer.takeFlyTime(inputTime);
                    }

                    ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.flytime.subCommands." + args[0] + ".message")
                            .replace("{player}", args[1])
                            .replace("{change}", String.valueOf(inputTime))
                            .replace("{amount}", String.valueOf(mhdfPlayer.getFlyTime()))
                    );
                    return;
                }
            }
        }

        {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.flytime.subCommands.help.message")
                    .replace("{helpList}", LangSetting.getSettingInstance().getHelpList("commands.flytime.subCommands"))
                    .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 2 &&
                (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("take"))
        ) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        if (args.length == 1) {
            return Arrays.asList("help", "set", "add", "take");
        }
        return new ArrayList<>();
    }
}
