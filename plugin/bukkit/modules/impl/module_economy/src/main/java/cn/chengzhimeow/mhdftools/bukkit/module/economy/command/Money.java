package cn.chengzhimeow.mhdftools.bukkit.module.economy.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.economy.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Money extends Command {
    public Money() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                LangSetting.getInstance().getConfig().commands().money().description(),
                "mhdftools.commands.money",
                false,
                ConfigSetting.getInstance().getConfig().moneyCommands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        MHDFToolsPlayer target = null;

        if (args.length == 0 && sender instanceof Player player) {
            target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
        }

        if (args.length == 1) {
            if (!sender.hasPermission("mhdftools.commands.money.other")) {
                sender.sendMessage(GlobalLangSetting.getInstance().getConfig().noPermission());
                return;
            }
            target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayerOrNull(args[0]);
        }

        if (target == null) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().money().usage())
                    .replace("{command}", label));
            return;
        }

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().money().message()
                .replace("{player}", target.getDisplayName())
                .replace("{amount}", target.getMoney().toPlainString())
                .replace("{money_name}", ConfigSetting.getInstance().getConfig().moneyName()));
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && sender.hasPermission("mhdftools.commands.money.other")) {
            return BungeeCordManager.getInstance().getPlayerList();
        }
        return new ArrayList<>();
    }
}
