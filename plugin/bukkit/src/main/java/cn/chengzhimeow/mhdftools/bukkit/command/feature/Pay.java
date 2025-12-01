package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.math.BigDecimalUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

final class Pay extends Command {
    public Pay() {
        super(
                null,
                List.of("economySettings.enable"),
                "转账",
                "mhdftools.commands.pay",
                true,
                ConfigSetting.getInstance().getData().getStringList("economySettings.payCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) {
            sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.pay.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (args[0].equals(sender.getName())) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.pay.paySelf"));
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        BigDecimal amount;
        try {
            amount = BigDecimalUtil.toBigDecimal(Double.parseDouble(args[1]));
        } catch (NumberFormatException e) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.pay.moneyFormatError"));
            return;
        }

        MHDFToolsPlayer mhdfPayPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender);
        if (mhdfPayPlayer.getMoney().compareTo(amount) < 0) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.pay.noMoney"));
            return;
        }
        mhdfPayPlayer.takeMoney(amount);

        BigDecimal tax = BigDecimalUtil.toBigDecimal(0);
        if (ConfigSetting.getInstance().getData().getBoolean("economySettings.personalIncomeTax.enable")) {
            tax = amount.multiply(BigDecimalUtil.toBigDecimal(
                    ConfigSetting.getInstance().getData().getDouble("economySettings.personalIncomeTax.rate"))
            );

            player.getPlayer().sendMessage(LangSetting.getInstance().i18n("economy.tax")
                    .replace("{amount}", String.valueOf(tax))
            );
        }

        MHDFToolsPlayer mhdfGetPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
        mhdfGetPlayer.addMoney(amount.subtract(tax));

        sender.sendMessage(LangSetting.getInstance().i18n("commands.pay.message")
                .replace("{player}", MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
                .replace("{amount}", String.valueOf(amount))
        );

        player.getPlayer().sendMessage(LangSetting.getInstance().i18n("commands.pay.receivedMessage")
                .replace("{player}", MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender).getDisplayName())
                .replace("{amount}", String.valueOf(amount))
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        return new ArrayList<>();
    }
}
