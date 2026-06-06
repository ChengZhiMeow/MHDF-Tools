package cn.chengzhimeow.mhdftools.bukkit.module.vanish.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.config.BossBarSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.vanish.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Vanish extends Command {
    public Vanish() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "隐身",
                "mhdftools.commands.vanish",
                false,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player target = null;
        boolean sendToSender = true;

        if (args.length == 0 && sender instanceof Player player) {
            target = player;
            sendToSender = false;
        }

        if (args.length == 1) {
            if (!sender.hasPermission("mhdftools.commands.vanish.give")) {
                sender.sendMessage(GlobalLangSetting.getInstance().getConfig().noPermission());
                return;
            }

            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
                return;
            }
        }

        if (target == null) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().vanish().usage())
                    .replace("{command}", label));
            return;
        }

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(target.getUniqueId());
        if (mhdfPlayer.isEnableVanish()) {
            mhdfPlayer.disableVanish();
            target.hideBossBar(BossBarSetting.getInstance().getVanish());

            TextComponent message = LangSetting.getInstance().getConfig().commands().vanish().message()
                    .replace("{player}", mhdfPlayer.getDisplayName())
                    .replace("{change}", GlobalLangSetting.getInstance().getConfig().disable());
            if (sendToSender && !sender.equals(target)) target.sendMessage(message);
            sender.sendMessage(message);
            return;
        } else {
            mhdfPlayer.enableVanish();
            if (ConfigSetting.getInstance().getConfig().bossbar().vanish().enable()) {
                target.showBossBar(BossBarSetting.getInstance().getVanish());
            }

            TextComponent message = LangSetting.getInstance().getConfig().commands().vanish().message()
                    .replace("{player}", mhdfPlayer.getDisplayName())
                    .replace("{change}", GlobalLangSetting.getInstance().getConfig().enable());
            if (sendToSender && !sender.equals(target)) target.sendMessage(message);
            sender.sendMessage(message);
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) return new ArrayList<>();
        if (!sender.hasPermission("mhdftools.commands.vanish.give")) return new ArrayList<>();

        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> !MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId()).isEnableVanish())
                .map(Player::getName)
                .toList();
    }
}
