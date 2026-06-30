package cn.chengzhimeow.mhdftools.bukkit.module.ignore.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.IgnoreData;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.ignore.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.ignore.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.ignore.config.LangSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class Ignore extends Command {
    public Ignore() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "屏蔽",
                "mhdftools.commands.ignore",
                true,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            StringBuilder list = new StringBuilder();
            List<IgnoreData> ignoreList = player.getIgnoreList();
            for (int i = 0; i < ignoreList.size(); i++) {
                OfflinePlayer ignorePlayer = Bukkit.getOfflinePlayer(ignoreList.get(i).getIgnore());
                list.append(ignorePlayer.getName() == null ? ignoreList.get(i).getIgnore() : ignorePlayer.getName());
                if (i != ignoreList.size() - 1) list.append(", ");
            }

            sender.sendMessage(LangSetting.getInstance().getConfig().commands().ignore().subCommands().list().message()
                    .replace("{list}", !list.isEmpty() ? list.toString() : "空"));
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
            if (!sender.hasPermission("mhdftools.bypass.ignore.blacklist")
                    && ConfigSetting.getInstance().getConfig().blacklist().contains(args[1])) {
                sender.sendMessage(LangSetting.getInstance().getConfig().commands().ignore().subCommands().add().blacklist()
                        .replace("{player}", args[1]));
                return;
            }

            MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(args[1]);
            if (player.isIgnore(target)) {
                sender.sendMessage(LangSetting.getInstance().getConfig().commands().ignore().subCommands().add().haveIgnore()
                        .replace("{player}", args[1]));
                return;
            }

            if (player.equals(target)) {
                sender.sendMessage(LangSetting.getInstance().getConfig().commands().ignore().subCommands().add().ignoreSelf());
                return;
            }

            player.ignore(target);
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().ignore().subCommands().add().message()
                    .replace("{player}", args[1]));
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(args[1]);
            if (!player.isIgnore(target)) {
                sender.sendMessage(LangSetting.getInstance().getConfig().commands().ignore().subCommands().remove().noIgnore()
                        .replace("{player}", args[1]));
                return;
            }

            player.deleteIgnore(target);
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().ignore().subCommands().remove().message()
                    .replace("{player}", args[1]));
            return;
        }

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().ignore().subCommands().help().message()
                .replace("{help_list}", ColorUtil.color(this.helpList(label)))
                .replace("{command}", label));
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return Arrays.asList("help", "list", "add", "remove");
        if (args.length == 2 && args[0].equalsIgnoreCase("add")) return null;
        if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
            return player.getIgnoreList().stream()
                    .map(data -> Bukkit.getOfflinePlayer(data.getIgnore()))
                    .map(OfflinePlayer::getName)
                    .filter(name -> name != null && !name.isBlank())
                    .toList();
        }
        return new ArrayList<>();
    }

    private String helpList(String label) {
        LangSetting.Config.Commands.Ignore.SubCommands subCommands = LangSetting.getInstance().getConfig().commands().ignore().subCommands();
        return String.join("\n",
                "&8▪ &f" + subCommands.help().usage().replace("{command}", label).toMiniMessageString() + " &7- " + subCommands.help().description().toMiniMessageString(),
                "&8▪ &f" + subCommands.list().usage().replace("{command}", label).toMiniMessageString() + " &7- " + subCommands.list().description().toMiniMessageString(),
                "&8▪ &f" + subCommands.add().usage().replace("{command}", label).toMiniMessageString() + " &7- " + subCommands.add().description().toMiniMessageString(),
                "&8▪ &f" + subCommands.remove().usage().replace("{command}", label).toMiniMessageString() + " &7- " + subCommands.remove().description().toMiniMessageString()
        );
    }
}
