package cn.chengzhimeow.mhdftools.bukkit.module.economy.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

final class MoneyAdmin extends Command {
    private static final List<String> SUB_COMMANDS = List.of("set", "add", "take");

    public MoneyAdmin() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "经济管理",
                "mhdftools.commands.moneyadmin",
                false,
                ConfigSetting.getInstance().getConfig().moneyadminCommands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 3 || !SUB_COMMANDS.contains(args[0])) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().moneyadmin().subCommands().help().message()
                    .replace("{help_list}", LangSetting.getInstance().getConfig().commands().moneyadmin().helpList())
                    .replace("{command}", label));
            return;
        }

        BigDecimal change;
        try {
            change = new BigDecimal(args[2]).setScale(2, RoundingMode.HALF_UP);
            if (change.signum() < 0 || (!args[0].equals("set") && change.signum() == 0)) {
                sender.sendMessage(LangSetting.getInstance().getConfig().commands().moneyadmin().moneyFormatError());
                return;
            }
        } catch (NumberFormatException ignored) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().moneyadmin().moneyFormatError());
            return;
        }

        MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(args[1]);
        switch (args[0]) {
            case "set" -> target.setMoney(change);
            case "add" -> target.addMoney(change);
            case "take" -> target.takeMoney(change);
            default -> {
                return;
            }
        }

        TextComponent message = switch (args[0]) {
            case "set" -> LangSetting.getInstance().getConfig().commands().moneyadmin().subCommands().set().message();
            case "add" -> LangSetting.getInstance().getConfig().commands().moneyadmin().subCommands().add().message();
            case "take" -> LangSetting.getInstance().getConfig().commands().moneyadmin().subCommands().take().message();
            default -> new TextComponent();
        };
        message = message
                .replace("{player}", target.getDisplayName())
                .replace("{change}", change.toPlainString())
                .replace("{amount}", target.getMoney().toPlainString())
                .replace("{money_name}", ConfigSetting.getInstance().getConfig().moneyName());

        sender.sendMessage(message);
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return LangSetting.getInstance().getConfig().commands().moneyadmin().subCommandNames();
        if (args.length == 2 && SUB_COMMANDS.contains(args[0])) return BungeeCordManager.getInstance().getPlayerList();
        return new ArrayList<>();
    }
}
