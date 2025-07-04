package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.GameModeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class GameMode extends AbstractCommand {
    public GameMode() {
        super(
                List.of("gamemodeSettings.enable"),
                "修改玩家游戏模式",
                "mhdftools.commands.gamemode",
                false,
                ConfigUtil.getConfig().getStringList("gamemodeSettings.commands").toArray(new String[0])
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
                ActionUtil.sendMessage(sender, LangUtil.i18n("playerOffline"));
                return;
            }
            player = Bukkit.getPlayer(args[1]);

            if (!sender.hasPermission("mhdftools.commands.gamemode.give")) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("noPermission"));
                return;
            }
        }

        // 输出帮助信息
        if (player == null) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.gamemode.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        org.bukkit.GameMode gameMode = GameModeUtil.getGameMode(args[0]);
        if (gameMode == null) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.gamemode.noGameMode")
                    .replace("{gamemode}", args[0])
            );
            return;
        }

        Main.instance.getBungeeCordManager().setGameMode(player, gameMode);
        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.gamemode.message")
                .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
                .replace("{gamemode}", LangUtil.i18n("gamemode." + gameMode.name()))
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return ConfigUtil.getConfig().getStringList("gamemodeSettings.tabCompleter");
        }
        if (args.length == 2) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        return new ArrayList<>();
    }
}
