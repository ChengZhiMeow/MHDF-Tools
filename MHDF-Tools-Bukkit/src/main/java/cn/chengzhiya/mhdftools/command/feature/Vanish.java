package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.database.data.VanishStatus;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.VanishStatusUtil;
import cn.chengzhiya.mhdftools.util.feature.VanishUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Vanish extends AbstractCommand {
    public Vanish() {
        super(
                List.of("vanishSettings.enable"),
                "隐身",
                "mhdftools.commands.vanish",
                false,
                ConfigUtil.getConfig().getStringList("vanishSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = null;
        boolean sendToSender = true;

        // 切换玩家自己的隐身模式
        if (args.length == 0 && sender instanceof Player) {
            sendToSender = false;
            player = (Player) sender;
        }

        // 切换其他玩家的隐身模式
        if (args.length == 1) {
            if (!sender.hasPermission("mhdftools.commands.vanish.give")) {
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
                    .replace("{usage}", LangUtil.i18n("commands.vanish.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        // 切换隐身
        VanishStatus vanishStatus = VanishStatusUtil.getVanishStatus(player);
        if (!vanishStatus.isEnable()) {
            VanishUtil.enableVanish(player);
            if (sendToSender) {
                VanishUtil.sendChangeVanishMessage(sender, player, true);
            }
            VanishUtil.sendChangeVanishMessage(player, player, true);
        } else {
            VanishUtil.disableVanish(player);
            if (sendToSender) {
                VanishUtil.sendChangeVanishMessage(sender, player, false);
            }
            VanishUtil.sendChangeVanishMessage(player, player, false);
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
