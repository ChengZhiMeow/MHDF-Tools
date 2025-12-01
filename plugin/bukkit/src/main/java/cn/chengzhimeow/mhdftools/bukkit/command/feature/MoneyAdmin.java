package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.math.BigDecimalUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

final class MoneyAdmin extends Command {
    public MoneyAdmin() {
        super(
                null,
                List.of("economySettings.enable"),
                "经济管理",
                "mhdftools.commands.moneyadmin",
                false,
                ConfigSetting.getInstance().getData().getStringList("economySettings.moneyadminCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 3) {
            switch (args[0]) {
                case "set", "add", "take" -> {
                    MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(args[1]);
                    BigDecimal change;
                    try {
                        change = BigDecimalUtil.toBigDecimal(Double.parseDouble(args[2]));
                    } catch (NumberFormatException e) {
                        sender.sendMessage(LangSetting.getInstance().i18n("commands.moneyadmin.moneyFormatError"));
                        return;
                    }

                    if (args[0].equals("set")) {
                        mhdfPlayer.setMoney(change);
                    }

                    if (args[0].equals("add")) {
                        mhdfPlayer.addMoney(change);
                    }

                    if (args[0].equals("take")) {
                        mhdfPlayer.takeMoney(change);
                    }

                    sender.sendMessage(LangSetting.getInstance().i18n("commands.moneyadmin.subCommands." + args[0] + ".message")
                            .replace("{player}", args[1])
                            .replace("{change}", change.toString())
                            .replace("{amount}", mhdfPlayer.getMoney().toString())
                    );
                    return;
                }
            }
        }

        // 输出帮助信息
        {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.moneyadmin.subCommands.help.message")
                    .replace("{helpList}", LangSetting.getInstance().getHelpList("commands.moneyadmin.subCommands"))
                    .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(LangSetting.getInstance().getKeys("commands.moneyadmin.subCommands"));
        }
        if (args.length == 2) {
            switch (args[0]) {
                case "set", "add", "take" -> {
                    return Main.instance.getBungeeCordManager().getPlayerList();
                }
            }
        }
        return new ArrayList<>();
    }
}
