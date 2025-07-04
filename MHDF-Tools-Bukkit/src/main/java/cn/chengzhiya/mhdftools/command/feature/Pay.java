package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.math.BigDecimalUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class Pay extends AbstractCommand {
    public Pay() {
        super(
                List.of("economySettings.enable"),
                "转账",
                "mhdftools.commands.pay",
                true,
                ConfigUtil.getConfig().getStringList("economySettings.payCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.pay.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (args[0].equals(sender.getName())) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.pay.paySelf"));
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        BigDecimal amount;
        try {
            amount = BigDecimalUtil.toBigDecimal(Double.parseDouble(args[1]));
        } catch (NumberFormatException e) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.pay.moneyFormatError"));
            return;
        }

        MHDFToolsPlayer mhdfPayPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(sender);
        if (mhdfPayPlayer.getMoney().compareTo(amount) < 0) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.pay.noMoney"));
            return;
        }
        mhdfPayPlayer.takeMoney(amount);

        BigDecimal tax = BigDecimalUtil.toBigDecimal(0);
        if (ConfigUtil.getConfig().getBoolean("economySettings.personalIncomeTax.enable")) {
            tax = amount.multiply(BigDecimalUtil.toBigDecimal(
                    ConfigUtil.getConfig().getDouble("economySettings.personalIncomeTax.rate"))
            );

            ActionUtil.sendMessage(player.getPlayer(), LangUtil.i18n("economy.tax")
                    .replace("{amount}", String.valueOf(tax))
            );
        }

        MHDFToolsPlayer mhdfGetPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        mhdfGetPlayer.addMoney(amount.subtract(tax));

        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.pay.message")
                .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
                .replace("{amount}", String.valueOf(amount))
        );

        ActionUtil.sendMessage(player.getPlayer(), LangUtil.i18n("commands.pay.receivedMessage")
                .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(sender).getDisplayName())
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
