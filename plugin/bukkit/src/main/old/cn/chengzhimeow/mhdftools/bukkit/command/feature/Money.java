package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Money extends Command {
    public Money() {
        super(
                null,
                List.of("economySettings.enable"),
                "查询余额",
                "mhdftools.commands.money",
                false,
                ConfigSetting.getInstance().getData().getStringList("economySettings.moneyCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        OfflinePlayer player = null;

        // 查询自己的余额
        if (args.length == 0 && sender instanceof Player) {
            player = (OfflinePlayer) sender;
        }

        // 查询其他玩家的余额
        if (args.length == 1) {
            if (!sender.hasPermission("mhdftools.commands.money.other")) {
                sender.sendMessage(LangSetting.getInstance().i18n("noPermission"));
                return;
            }

            player = Bukkit.getOfflinePlayer(args[0]);
        }

        // 输出帮助信息
        if (player == null) {
            sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.money.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
        sender.sendMessage(LangSetting.getInstance().i18n("commands.money.message")
                .replace("{player}", MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
                .replace("{amount}", mhdfPlayer.getMoney().toString())
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        return new ArrayList<>();
    }
}
