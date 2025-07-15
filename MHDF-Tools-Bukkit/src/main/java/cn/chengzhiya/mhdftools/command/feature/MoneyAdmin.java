package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.math.BigDecimalUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

final class MoneyAdmin extends AbstractCommand {
    public MoneyAdmin() {
        super(
                List.of("economySettings.enable"),
                "经济管理",
                "mhdftools.commands.moneyadmin",
                false,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("economySettings.moneyadminCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 3) {
            switch (args[0]) {
                case "set", "add", "take" -> {
                    MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(args[1]);
                    BigDecimal change;
                    try {
                        change = BigDecimalUtil.toBigDecimal(Double.parseDouble(args[2]));
                    } catch (NumberFormatException e) {
                        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.moneyadmin.moneyFormatError"));
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

                    ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.moneyadmin.subCommands." + args[0] + ".message")
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
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.moneyadmin.subCommands.help.message")
                    .replace("{helpList}", Main.instance.getConfigManager().getLangManager().getHelpList("commands.moneyadmin.subCommands"))
                    .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(Main.instance.getConfigManager().getLangManager().getKeys("commands.moneyadmin.subCommands"));
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
