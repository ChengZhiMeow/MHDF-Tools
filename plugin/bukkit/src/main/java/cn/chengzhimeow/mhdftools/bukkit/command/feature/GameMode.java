package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.GameModeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class GameMode extends Command {
    public GameMode() {
        super(
                null,
                List.of("gamemodeSettings.enable"),
                "修改玩家游戏模式",
                "mhdftools.commands.gamemode",
                false,
                ConfigSetting.getInstance().getData().getStringList("gamemodeSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = null;

        // 切换玩家自己的游戏模式
        if (args.length == 1 && sender instanceof Player) {
            player = (Player) sender;
        }

        // 切换其他玩家的游戏模式
        if (args.length == 2) {
            if (Bukkit.getPlayer(args[1]) == null) {
                sender.sendMessage(LangSetting.getInstance().i18n("playerOffline"));
                return;
            }
            player = Bukkit.getPlayer(args[1]);

            if (!sender.hasPermission("mhdftools.commands.gamemode.give")) {
                sender.sendMessage(LangSetting.getInstance().i18n("noPermission"));
                return;
            }
        }

        // 输出帮助信息
        if (player == null) {
            sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.gamemode.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        org.bukkit.GameMode gameMode = GameModeUtil.getGameMode(args[0]);
        if (gameMode == null) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.gamemode.noGameMode")
                    .replace("{gamemode}", args[0])
            );
            return;
        }

        Main.instance.getBungeeCordManager().setGameMode(player, gameMode);
        sender.sendMessage(LangSetting.getInstance().i18n("commands.gamemode.message")
                .replace("{player}", MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
                .replace("{gamemode}", LangSetting.getInstance().i18n("gamemode." + gameMode.name()))
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return ConfigSetting.getInstance().getData().getStringList("gamemodeSettings.tabCompleter");
        }
        if (args.length == 2) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        return new ArrayList<>();
    }
}
