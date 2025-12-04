package cn.chengzhimeow.mhdftools.bukkit.module.ip.command;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.ip.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.ip.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.ip.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.ip.util.IpLocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

final class Ip extends Command {
    public Ip() {
        super(
                ModuleMain.instance,
                List.of("enable"),
                "查询IP信息",
                "mhdftools.commands.ip",
                false,
                ConfigSetting.getInstance().getData().getStringList("commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(LangSetting.getInstance().i18n("usage_error")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.ip.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(LangSetting.getInstance().i18n("player_offline"));
            return;
        }

        String ip = Objects.requireNonNull(player.getAddress()).getHostString();
        sender.sendMessage(LangSetting.getInstance().i18n("commands.ip.message")
                .replace("{player}", player.getName())
                .replace("{ip}", ip)
                .replace("{location}", IpLocationUtil.getIpLocation(ip))
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return MHDFToolsBukkit.getInstance().getBungeeCordManager().getBukkitPlayerList();
        return super.tabCompleter(sender, label, args);
    }
}