package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.bukkit.command.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.FakeChangeTimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class FakeChangeTime extends Command {
    public FakeChangeTime() {
        super(
                List.of("fakeChangeTimeSettings.enable"),
                "虚假调节时间",
                "mhdftools.commands.fakechangetime",
                false,
                ConfigSetting.getSettingInstance().getData().getStringList("fakeChangeTimeSettings.commands").toArray(new String[0])
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
                ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("noPermission"));
                return;
            }
            if (Bukkit.getPlayer(args[1]) == null) {
                ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("playerOffline"));
                return;
            }
            player = Bukkit.getPlayer(args[1]);
        }

        // 输出帮助信息
        if (player == null) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.fakechangetime.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (args[0].equals("reset")) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.fakechangetime.reset")
                    .replace("{player}", player.getName())
            );
            player.resetPlayerTime();
            return;
        }

        long time;
        try {
            time = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.fakechangetime.timeFormatError"));
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
