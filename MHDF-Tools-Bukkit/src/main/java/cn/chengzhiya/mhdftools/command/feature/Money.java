package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.EconomyUtil;
import cn.chengzhiya.mhdftools.util.feature.NickUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class Money extends AbstractCommand {
    public Money() {
        super(
                "economySettings.enable",
                "查询余额",
                "mhdftools.commands.money",
                false,
                ConfigUtil.getConfig().getStringList("economySettings.moneyCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        String name = null;

        // 查询自己的余额
        if (args.length == 0 && sender instanceof Player) {
            name = sender.getName();
        }

        // 查询其他玩家的余额
        if (args.length == 1) {
            if (!sender.hasPermission("mhdftools.commands.money.other")) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("noPermission"));
                return;
            }

            name = args[0];
        }

        // 输出帮助信息
        if (name == null) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.money.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        BigDecimal money = EconomyUtil.getMoney(player);
        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.money.message")
                .replace("{player}", NickUtil.getName(player))
                .replace("{amount}", money.toString())
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
