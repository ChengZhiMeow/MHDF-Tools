package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.database.data.FlyStatus;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.FlyStatusUtil;
import cn.chengzhiya.mhdftools.util.feature.FlyUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Fly extends AbstractCommand {
    public Fly() {
        super(
                List.of("flySettings.enable"),
                "飞行",
                "mhdftools.commands.fly",
                false,
                ConfigUtil.getConfig().getStringList("flySettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = null;
        boolean sendToSender = true;

        // 切换玩家自己的飞行模式
        if (args.length == 0 && sender instanceof Player) {
            sendToSender = false;
            player = (Player) sender;

            FlyStatus flyStatus = FlyStatusUtil.getFlyStatus(player);
            if (!sender.hasPermission("mhdftools.commands.fly.infinite") && flyStatus.getTime() <= 0) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("noPermission"));
                return;
            }
        }

        // 切换其他玩家的飞行模式
        if (args.length == 1) {
            if (!sender.hasPermission("mhdftools.commands.fly.give")) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("noPermission"));
                return;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("playerOffline"));
                return;
            }
            player = Bukkit.getPlayer(args[0]);
        }

        // 输出帮助信息
        if (player == null) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.fly.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        // 切换飞行
        FlyStatus flyStatus = FlyStatusUtil.getFlyStatus(player);
        if (!flyStatus.isEnable()) {
            FlyUtil.enableFly(player);
            if (sendToSender) {
                FlyUtil.sendChangeFlyMessage(sender, player, true);
            }
            FlyUtil.sendChangeFlyMessage(player, player, true);
        } else {
            FlyUtil.disableFly(player);
            if (sendToSender) {
                FlyUtil.sendChangeFlyMessage(sender, player, false);
            }
            FlyUtil.sendChangeFlyMessage(player, player, false);
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        return new ArrayList<>();
    }
}
