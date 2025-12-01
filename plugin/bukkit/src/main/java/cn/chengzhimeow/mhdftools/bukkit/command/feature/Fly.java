package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Fly extends Command {
    public Fly() {
        super(
                null,
                List.of("flySettings.enable"),
                "飞行",
                "mhdftools.commands.fly",
                false,
                ConfigSetting.getInstance().getData().getStringList("flySettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = null;
        boolean changeOther = true;

        // 切换玩家自己的飞行模式
        if (args.length == 0 && sender instanceof Player) {
            changeOther = false;
            player = (Player) sender;

            MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
            if (!sender.hasPermission("mhdftools.commands.fly.infinite") && mhdfPlayer.getFlyTime() <= 0) {
                sender.sendMessage(LangSetting.getInstance().i18n("noPermission"));
                return;
            }
        }

        // 切换其他玩家的飞行模式
        if (args.length == 1) {
            if (!sender.hasPermission("mhdftools.commands.fly.other")) {
                sender.sendMessage(LangSetting.getInstance().i18n("noPermission"));
                return;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage(LangSetting.getInstance().i18n("playerOffline"));
                return;
            }
            player = Bukkit.getPlayer(args[0]);
        }

        // 输出帮助信息
        if (player == null) {
            sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.fly.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
        if (!mhdfPlayer.isEnableFly()) {
            if (changeOther) {
                mhdfPlayer.setFlyTime(Integer.MAX_VALUE);
                this.sendChangeFlyMessage(sender, player, true);
            }
            this.sendChangeFlyMessage(player, player, true);
            mhdfPlayer.enableFly();
            return;
        }

        if (changeOther) {
            mhdfPlayer.setFlyTime(0);
            this.sendChangeFlyMessage(sender, player, false);
        }
        this.sendChangeFlyMessage(player, player, false);
        mhdfPlayer.disableFly();
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        return new ArrayList<>();
    }

    /**
     * 给指定目标实例发送切换飞行的提示
     *
     * @param sender 接收信息的目标实例
     * @param player 调整飞行的玩家实例
     * @param enable 是否开启飞行
     */
    private void sendChangeFlyMessage(CommandSender sender, Player player, boolean enable) {
        sender.sendMessage(
                LangSetting.getInstance().i18n("commands.fly.message")
                        .replace("{player}", MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
                        .replace("{change}",
                                enable ? LangSetting.getInstance().i18n("enable") : LangSetting.getInstance().i18n("disable")
                        )
        );
    }
}
