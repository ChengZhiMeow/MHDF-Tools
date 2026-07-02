package cn.chengzhimeow.mhdftools.bukkit.module.fly.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.fly.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.fly.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.fly.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Fly extends Command {
    public Fly() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "飞行",
                "mhdftools.commands.fly",
                false,
                ConfigSetting.getInstance().getConfig().flyCommands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player target = null;
        boolean sendToSender = true;

        if (args.length == 0 && sender instanceof Player player) {
            target = player;
            sendToSender = false;

            MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
            if (!sender.hasPermission("mhdftools.commands.fly.infinite") && mhdfPlayer.getFlyTime() <= 0) {
                sender.sendMessage(GlobalLangSetting.getInstance().getConfig().noPermission());
                return;
            }
        }

        if (args.length == 1) {
            if (!sender.hasPermission("mhdftools.commands.fly.other")) {
                sender.sendMessage(GlobalLangSetting.getInstance().getConfig().noPermission());
                return;
            }

            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
                return;
            }
        }

        if (target == null) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().fly().usage())
                    .replace("{command}", label));
            return;
        }

        MHDFToolsPlayer targetMhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(target.getUniqueId(), target.getName());
        if (targetMhdfPlayer.isEnableFly()) {
            if (sendToSender) targetMhdfPlayer.setFlyTime(0);
            targetMhdfPlayer.disableFly();
            this.sendChangeFlyMessage(sender, target, false);
            if (sendToSender && !sender.equals(target)) this.sendChangeFlyMessage(target, target, false);
            return;
        }

        if (sendToSender) targetMhdfPlayer.setFlyTime(Integer.MAX_VALUE);
        targetMhdfPlayer.enableFly();
        this.sendChangeFlyMessage(sender, target, true);
        if (sendToSender && !sender.equals(target)) this.sendChangeFlyMessage(target, target, true);
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) return new ArrayList<>();
        if (!sender.hasPermission("mhdftools.commands.fly.other")) return new ArrayList<>();
        return null;
    }

    private void sendChangeFlyMessage(CommandSender sender, Player target, boolean enable) {
        MHDFToolsPlayer targetMhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(target.getUniqueId(), target.getName());
        TextComponent message = LangSetting.getInstance().getConfig().commands().fly().message()
                .replace("{player}", targetMhdfPlayer.getDisplayName())
                .replace("{change}", enable ? GlobalLangSetting.getInstance().getConfig().enable() : GlobalLangSetting.getInstance().getConfig().disable());
        sender.sendMessage(message);
    }
}
