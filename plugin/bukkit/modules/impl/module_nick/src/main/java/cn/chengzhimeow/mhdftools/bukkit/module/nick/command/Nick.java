package cn.chengzhimeow.mhdftools.bukkit.module.nick.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.nick.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.nick.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.nick.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Nick extends Command {
    public Nick() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "匿名",
                "mhdftools.commands.nick",
                false,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player target = null;
        boolean sendToSender = true;

        if (args.length == 1 && sender instanceof Player player) {
            target = player;
            sendToSender = false;
        }

        if (args.length == 2) {
            if (!sender.hasPermission("mhdftools.commands.nick.give")) {
                sender.sendMessage(GlobalLangSetting.getInstance().getConfig().noPermission());
                return;
            }

            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
                return;
            }
        }

        if (target == null) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().nick().usage())
                    .replace("{command}", label));
            return;
        }

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(target.getUniqueId());
        if (args[0].equals("off")) {
            mhdfPlayer.deleteNick();

            TextComponent message = LangSetting.getInstance().getConfig().commands().nick().message()
                    .replace("{player}", target.getName())
                    .replace("{name}", ColorUtil.color(args[0]));
            if (sendToSender && !sender.equals(target)) target.sendMessage(message);
            sender.sendMessage(message);
        } else {
            mhdfPlayer.setNick(args[0]);

            TextComponent message = LangSetting.getInstance().getConfig().commands().nick().message()
                    .replace("{player}", target.getName())
                    .replace("{name}", ColorUtil.color(args[0]));
            if (sendToSender && !sender.equals(target)) target.sendMessage(message);
            sender.sendMessage(message);
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return List.of("off");
        if (args.length == 2 && sender.hasPermission("mhdftools.commands.nick.give")) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .toList();
        }
        return new ArrayList<>();
    }
}
