package cn.chengzhimeow.mhdftools.bukkit.module.economy.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

final class Pay extends Command {
    public Pay() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "转账",
                "mhdftools.commands.pay",
                true,
                ConfigSetting.getInstance().getConfig().payCommands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().pay().usage())
                    .replace("{command}", label));
            return;
        }

        if (args[0].equalsIgnoreCase(sender.getName())) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().pay().paySelf());
            return;
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(args[1]).setScale(2, RoundingMode.HALF_UP);
            if (amount.signum() <= 0) {
                sender.sendMessage(LangSetting.getInstance().getConfig().commands().pay().moneyFormatError());
                return;
            }
        } catch (NumberFormatException ignored) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().pay().moneyFormatError());
            return;
        }

        MHDFToolsPlayer source = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
        if (source.getMoney().compareTo(amount) < 0) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().pay().noMoney());
            return;
        }

        MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(args[0]);
        BigDecimal tax = BigDecimal.ZERO;
        if (ConfigSetting.getInstance().getConfig().personalIncomeTax().enable()) {
            tax = amount.multiply(ConfigSetting.getInstance().getConfig().personalIncomeTax().rate());

            TextComponent taxMessage = LangSetting.getInstance().getConfig().economy().tax()
                    .replace("{amount}", tax.toPlainString())
                    .replace("{money_name}", ConfigSetting.getInstance().getConfig().moneyName());
            target.sendMessage(taxMessage);
        }
        BigDecimal received = amount.subtract(tax);

        source.takeMoney(amount);
        target.addMoney(received);

        TextComponent sendMessage = LangSetting.getInstance().getConfig().commands().pay().message()
                .replace("{player}", target.getDisplayName())
                .replace("{amount}", amount.toPlainString())
                .replace("{money_name}", ConfigSetting.getInstance().getConfig().moneyName());
        sender.sendMessage(sendMessage);

        TextComponent receivedMessage = LangSetting.getInstance().getConfig().commands().pay().receivedMessage()
                .replace("{player}", source.getDisplayName())
                .replace("{amount}", received.toPlainString())
                .replace("{money_name}", ConfigSetting.getInstance().getConfig().moneyName());
        target.sendMessage(receivedMessage);
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return BungeeCordManager.getInstance().getPlayerList();
        return new ArrayList<>();
    }
}
