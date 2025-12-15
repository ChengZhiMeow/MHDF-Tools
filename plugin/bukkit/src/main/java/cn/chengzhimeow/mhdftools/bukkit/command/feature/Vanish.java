package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Vanish extends Command {
    public Vanish() {
        super(
                null,
                List.of("vanishSettings.enable"),
                "隐身",
                "mhdftools.commands.vanish",
                false,
                ConfigSetting.getInstance().getData().getStringList("vanishSettings.commands").toArray(new String[0])
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
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.vanish.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        // 切换隐身
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
        if (!mhdfPlayer.isEnableVanish()) {
            if (sendToSender) {
                this.sendChangeVanishMessage(sender, player, true);
            }
            this.sendChangeVanishMessage(player, player, true);
            mhdfPlayer.enableVanish();
        } else {
            if (sendToSender) {
                this.sendChangeVanishMessage(sender, player, false);
            }
            this.sendChangeVanishMessage(player, player, false);
            mhdfPlayer.disableVanish();
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
     * @param player 调整隐身的玩家实例
     * @param enable 是否开启隐身
     */
    private void sendChangeVanishMessage(CommandSender sender, Player player, boolean enable) {
        sender.sendMessage(LangSetting.getInstance().i18n("commands.vanish.message")
                .replace("{player}", MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
                .replace("{change}",
                        enable ? LangSetting.getInstance().i18n("enable") : LangSetting.getInstance().i18n("disable")
                )
        );
    }
}
