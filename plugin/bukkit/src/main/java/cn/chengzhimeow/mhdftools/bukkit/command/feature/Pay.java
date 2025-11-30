package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
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
                ConfigSetting.getSettingInstance().getData().getStringList("economySettings.payCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.pay.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (args[0].equals(sender.getName())) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.pay.paySelf"));
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        BigDecimal amount;
        try {
            amount = BigDecimalUtil.toBigDecimal(Double.parseDouble(args[1]));
        } catch (NumberFormatException e) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.pay.moneyFormatError"));
            return;
        }

        MHDFToolsPlayer mhdfPayPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(sender);
        if (mhdfPayPlayer.getMoney().compareTo(amount) < 0) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.pay.noMoney"));
            return;
        }
        mhdfPayPlayer.takeMoney(amount);

        BigDecimal tax = BigDecimalUtil.toBigDecimal(0);
        if (ConfigSetting.getSettingInstance().getData().getBoolean("economySettings.personalIncomeTax.enable")) {
            tax = amount.multiply(BigDecimalUtil.toBigDecimal(
                    ConfigSetting.getSettingInstance().getData().getDouble("economySettings.personalIncomeTax.rate"))
            );

            ActionUtil.sendMessage(player.getPlayer(), LangSetting.getSettingInstance().i18n("economy.tax")
                    .replace("{amount}", String.valueOf(tax))
            );
        }

        MHDFToolsPlayer mhdfGetPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        mhdfGetPlayer.addMoney(amount.subtract(tax));

        ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.pay.message")
                .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
                .replace("{amount}", String.valueOf(amount))
        );

        ActionUtil.sendMessage(player.getPlayer(), LangSetting.getSettingInstance().i18n("commands.pay.receivedMessage")
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
