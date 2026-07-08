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
import net.kyori.adventure.text.Component;
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
                    .replace("{help_list}", this.getHelpMessage(label))
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

        MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayerOrNull(args[1]);
        if (target == null) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerNotFound());
            return;
        }

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
        if (args.length == 1) return List.of("help", "set", "add", "take");
        if (args.length == 2 && SUB_COMMANDS.contains(args[0])) return BungeeCordManager.getInstance().getPlayerList();
        return new ArrayList<>();
    }

    private Component getHelpMessage(String label) {
        return Component.empty()
                .append(this.getSubCommandInfo(
                        LangSetting.getInstance().getConfig().commands().moneyadmin().subCommands().help().usage(),
                        LangSetting.getInstance().getConfig().commands().moneyadmin().subCommands().help().description(),
                        label
                ))
                .appendNewline()
                .append(this.getSubCommandInfo(
                        LangSetting.getInstance().getConfig().commands().moneyadmin().subCommands().set().usage(),
                        LangSetting.getInstance().getConfig().commands().moneyadmin().subCommands().set().description(),
                        label
                ))
                .appendNewline()
                .append(this.getSubCommandInfo(
                        LangSetting.getInstance().getConfig().commands().moneyadmin().subCommands().add().usage(),
                        LangSetting.getInstance().getConfig().commands().moneyadmin().subCommands().add().description(),
                        label
                ))
                .appendNewline()
                .append(this.getSubCommandInfo(
                        LangSetting.getInstance().getConfig().commands().moneyadmin().subCommands().take().usage(),
                        LangSetting.getInstance().getConfig().commands().moneyadmin().subCommands().take().description(),
                        label
                ));
    }

    private TextComponent getSubCommandInfo(TextComponent usage, TextComponent description, String label) {
        return LangSetting.getInstance().getConfig().commandInfoFormat()
                .replace("{usage}", usage.replace("{command}", label))
                .replace("{description}", description);
    }
}
