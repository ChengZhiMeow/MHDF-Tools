package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.database.data.FlyStatus;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.FlyStatusUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FlyTime extends AbstractCommand {
    public FlyTime() {
        super(
                List.of("flySettings.enable"),
                "限时飞行",
                "mhdftools.commands.flytime",
                false,
                ConfigUtil.getConfig().getStringList("flySettings.flytimeCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 3) {
            switch (args[0]) {
                case "set", "add", "take" -> {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

                    long inputTime;
                    try {
                        inputTime = Long.parseLong(args[2]);
                    } catch (NumberFormatException e) {
                        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.flytime.timeFormatError"));
                        return;
                    }

                    FlyStatus flyStatus = FlyStatusUtil.getFlyStatus(player);

                    if (args[0].equals("set")) {
                        flyStatus.setTime(inputTime);
                    }

                    if (args[0].equals("add")) {
                        flyStatus.setTime(flyStatus.getTime() + inputTime);
                    }

                    if (args[0].equals("take")) {
                        flyStatus.setTime(flyStatus.getTime() - inputTime);
                    }

                    FlyStatusUtil.updateFlyStatus(flyStatus);

                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.flytime.subCommands." + args[0] + ".message")
                            .replace("{player}", args[1])
                            .replace("{change}", String.valueOf(inputTime))
                            .replace("{amount}", String.valueOf(flyStatus.getTime()))
                    );
                    return;
                }
            }
        }

        {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.flytime.subCommands.help.message")
                    .replace("{helpList}", LangUtil.getHelpList("commands.flytime.subCommands"))
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
