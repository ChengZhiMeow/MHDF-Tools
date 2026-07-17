package cn.chengzhimeow.mhdftools.bukkit.module.ignore.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.IgnoreData;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.ignore.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.ignore.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.ignore.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import net.kyori.adventure.text.Component;
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
                LangSetting.getInstance().getConfig().commands().ignore().description(),
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
                if (i != 0) list.append(", ");
                OfflinePlayer ignorePlayer = Bukkit.getOfflinePlayer(ignoreList.get(i).getIgnore());
                list.append(ignorePlayer.getName() == null ? ignoreList.get(i).getIgnore() : ignorePlayer.getName());
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

            MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayerOrNull(args[1]);
            if (target == null) {
                sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerNotFound());
                return;
            }

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
            MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayerOrNull(args[1]);
            if (target == null) {
                sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerNotFound());
                return;
            }

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
                .replace("{help_list}", this.getHelpMessage(label))
                .replace("{command}", label));
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return Arrays.asList("help", "list", "add", "remove");
        if (args.length == 2 && args[0].equalsIgnoreCase("add")) return null;
        if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            MHDFToolsPlayer player = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
            return player.getIgnoreList().stream()
                    .map(data -> MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(data.getPlayer()))
                    .map(MHDFToolsPlayer::getNameOrNull)
                    .filter(name -> name != null && !name.isBlank())
                    .toList();
        }
        return new ArrayList<>();
    }

    private Component getHelpMessage(String label) {
        return Component.empty()
                .append(this.getSubCommandInfo(
                        LangSetting.getInstance().getConfig().commands().ignore().subCommands().help().usage(),
                        LangSetting.getInstance().getConfig().commands().ignore().subCommands().help().description(),
                        label
                ))
                .appendNewline()
                .append(this.getSubCommandInfo(
                        LangSetting.getInstance().getConfig().commands().ignore().subCommands().list().usage(),
                        LangSetting.getInstance().getConfig().commands().ignore().subCommands().list().description(),
                        label
                ))
                .appendNewline()
                .append(this.getSubCommandInfo(
                        LangSetting.getInstance().getConfig().commands().ignore().subCommands().add().usage(),
                        LangSetting.getInstance().getConfig().commands().ignore().subCommands().add().description(),
                        label
                ))
                .appendNewline()
                .append(this.getSubCommandInfo(
                        LangSetting.getInstance().getConfig().commands().ignore().subCommands().remove().usage(),
                        LangSetting.getInstance().getConfig().commands().ignore().subCommands().remove().description(),
                        label
                ));
    }

    private TextComponent getSubCommandInfo(TextComponent usage, TextComponent description, String label) {
        return LangSetting.getInstance().getConfig().commandInfoFormat()
                .replace("{usage}", usage.replace("{command}", label))
                .replace("{description}", description);
    }
}
