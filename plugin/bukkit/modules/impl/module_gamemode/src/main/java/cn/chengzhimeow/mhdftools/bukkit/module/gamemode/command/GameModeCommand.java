package cn.chengzhimeow.mhdftools.bukkit.module.gamemode.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.common.message.Messager;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.gamemode.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.gamemode.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.gamemode.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.gamemode.message.GameModeMessage;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class GameModeCommand extends Command {
    public GameModeCommand() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "修改玩家游戏模式",
                "mhdftools.commands.gamemode",
                false,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        String targetName = null;
        Player target = null;

        // 切换玩家自己的游戏模式
        if (args.length == 1 && sender instanceof Player player) {
            targetName = player.getName();
            target = player;
        }

        // 切换其他玩家的游戏模式
        if (args.length == 2) {
            if (!sender.hasPermission("mhdftools.commands.gamemode.give")) {
                sender.sendMessage(GlobalLangSetting.getInstance().getConfig().noPermission());
                return;
            }

            targetName = args[1];
            if (!BungeeCordManager.getInstance().ifPlayerOnline(targetName)) {
                sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
                return;
            }
            target = Bukkit.getPlayerExact(targetName);
        }

        // 输出帮助信息
        if (targetName == null) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().gamemode().usage())
                    .replace("{command}", label));
            return;
        }

        // 通过文本获取游戏模式实例
        GameMode gameMode = ConfigSetting.getInstance().getConfig().getGameMode(args[0]);
        if (gameMode == null) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().gamemode().noGameMode()
                    .replace("{gamemode}", args[0]));
            return;
        }

        // 修改本服玩家游戏模式
        if (target != null) {
            MHDFToolsPlayer mhdfTarget = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(target.getUniqueId(), target.getName());
            TextComponent message = LangSetting.getInstance().getConfig().commands().gamemode().message()
                    .replace("{player}", mhdfTarget.getDisplayName())
                    .replace("{gamemode}", LangSetting.getInstance().getConfig().gameModeName(gameMode));

            target.setGameMode(gameMode);
            if (!sender.equals(target)) sender.sendMessage(message);
            mhdfTarget.sendMessage(message);
        } else {
            // 修改跨服玩家游戏模式
            if (!BungeeCordManager.getInstance().isBungeeCordMode()) return;

            TextComponent message = LangSetting.getInstance().getConfig().commands().gamemode().message()
                    .replace("{player}", targetName)
                    .replace("{gamemode}", LangSetting.getInstance().getConfig().gameModeName(gameMode));
            Messager.publish(
                    new GameModeMessage(BungeeCordManager.getInstance().getServerName(), targetName, gameMode.name())
            );
            sender.sendMessage(message);
            MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(targetName).sendMessage(message);
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return ConfigSetting.getInstance().getConfig().tabCompleter();
        }
        if (args.length == 2 && sender.hasPermission("mhdftools.commands.gamemode.give")) {
            return BungeeCordManager.getInstance().getPlayerList();
        }
        return new ArrayList<>();
    }
}
