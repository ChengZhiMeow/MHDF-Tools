package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.FakeChangeTimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class FakeChangeTime extends AbstractCommand {
    public FakeChangeTime() {
        super(
                "fakeChangeTimeSettings.enable",
                "虚假调节时间",
                "mhdftools.commands.fakechangetime",
                false,
                ConfigUtil.getConfig().getStringList("fakeChangeTimeSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = null;
        boolean sendToSender = true;

        // 修改自己的虚假时间
        if (args.length == 1 && sender instanceof Player) {
            sendToSender = false;
            player = (Player) sender;
        }

        // 修改其他玩家的的虚假时间
        if (args.length == 2) {
            if (!sender.hasPermission("mhdftools.commands.fakechangetime.other")) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("noPermission"));
                return;
            }
            if (Bukkit.getPlayer(args[1]) == null) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("playerOffline"));
                return;
            }
            player = Bukkit.getPlayer(args[1]);
        }

        // 输出帮助信息
        if (player == null) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.fakechangetime.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (args[0].equals("reset")) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.fakechangetime.reset"));
            player.resetPlayerTime();
            return;
        }

        long time;
        try {
            time = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.fakechangetime.timeFormatError"));
            return;
        }

        player.setPlayerTime(time, false);
        if (sendToSender) {
            FakeChangeTimeUtil.sendFakeChangeTimeMessage(sender, player, time);
        }
        FakeChangeTimeUtil.sendFakeChangeTimeMessage(player, player, time);
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("reset");
        }
        return new ArrayList<>();
    }
}
