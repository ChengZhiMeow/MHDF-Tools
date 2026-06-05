package cn.chengzhimeow.mhdftools.bukkit.module.ip.command;

import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.ip.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.ip.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.ip.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.ip.util.IpLocationUtil;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
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
                ConfigSetting.getInstance().getConfig().enable(),
                "查询IP信息",
                "mhdftools.commands.ip",
                false,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().ip().usage())
                    .replace("{command}", label));
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
            return;
        }

        String ip = Objects.requireNonNull(player.getAddress()).getHostString();
        sender.sendMessage(LangSetting.getInstance().getConfig().commands().ip().message()
                .replace("{player}", player.getName())
                .replace("{ip}", ip)
                .replace("{location}", IpLocationUtil.getIpLocation(ip)));
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return BungeeCordManager.getInstance().getBukkitPlayerList();
        return super.tabCompleter(sender, label, args);
    }
}
