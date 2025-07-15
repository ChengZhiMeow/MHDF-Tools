package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Vanish extends AbstractCommand {
    public Vanish() {
        super(
                List.of("vanishSettings.enable"),
                "隐身",
                "mhdftools.commands.vanish",
                false,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("vanishSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = null;
        boolean sendToSender = true;

        // 切换玩家自己的隐身模式
        if (args.length == 0 && sender instanceof Player) {
            sendToSender = false;
            player = (Player) sender;
        }

        // 切换其他玩家的隐身模式
        if (args.length == 1) {
            if (!sender.hasPermission("mhdftools.commands.vanish.give")) {
                ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("noPermission"));
                return;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("playerOffline"));
                return;
            }
            player = Bukkit.getPlayer(args[0]);
        }

        // 输出帮助信息
        if (player == null) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                    .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.vanish.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        // 切换隐身
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        if (!mhdfPlayer.isEnableVanish()) {
            mhdfPlayer.enableVanish();
            if (sendToSender) {
                this.sendChangeVanishMessage(sender, player, true);
            }
            this.sendChangeVanishMessage(player, player, true);
        } else {
            mhdfPlayer.disableVanish();
            if (sendToSender) {
                this.sendChangeVanishMessage(sender, player, false);
            }
            this.sendChangeVanishMessage(player, player, false);
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        return new ArrayList<>();
    }

    /**
     * 给指定目标实例发送切换隐身的提示
     *
     * @param sender 接收信息的目标实例
     * @param player 开启隐身的玩家实例
     * @param enable 是否开启隐身
     */
    private void sendChangeVanishMessage(CommandSender sender, Player player, boolean enable) {
        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.vanish.message")
                .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
                .replace("{change}",
                        enable ? Main.instance.getConfigManager().getLangManager().i18n("enable") : Main.instance.getConfigManager().getLangManager().i18n("disable")
                )
        );
    }
}
