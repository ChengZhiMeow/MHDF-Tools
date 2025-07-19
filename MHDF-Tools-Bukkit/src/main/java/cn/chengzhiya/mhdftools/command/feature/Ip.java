package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.Command;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.feature.IpUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

final class Ip extends Command {
    public Ip() {
        super(
                List.of("ipSettings.enable"),
                "查询IP信息",
                "mhdftools.commands.ip",
                false,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("ipSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                    .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.ip.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("playerOffline"));
            return;
        }

        String ip = Objects.requireNonNull(player.getAddress()).getHostString();

        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.ip.message")
                .replace("{player}", player.getName())
                .replace("{ip}", ip)
                .replace("{location}", IpUtil.getIpLocation(ip))
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return null;
        }
        return super.tabCompleter(sender, label, args);
    }
}
