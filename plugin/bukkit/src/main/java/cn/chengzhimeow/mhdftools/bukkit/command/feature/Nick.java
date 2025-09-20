package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.command.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.message.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Nick extends Command {
    public Nick() {
        super(
                List.of("nickSettings.enable"),
                "匿名",
                "mhdftools.commands.nick",
                false,
                ConfigSetting.getSettingInstance().getData().getStringList("nickSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = null;

        // 修改玩家自己的匿名名称
        if (args.length == 1 && sender instanceof Player) {
            player = (Player) sender;
        }

        // 修改其他玩家的匿名名称
        if (args.length >= 2) {
            if (Bukkit.getPlayer(args[1]) == null) {
                ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("playerOffline"));
                return;
            }
            if (!sender.hasPermission("mhdftools.commands.nick.give")) {
                ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("noPermission"));
                return;
            }
            player = Bukkit.getPlayer(args[1]);
        }

        // 输出帮助信息
        if (player == null) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.nick.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player);
        if (args[0].equals("off")) {
            mhdfPlayer.deleteNick();
        } else {
            mhdfPlayer.setNick(args[0]);
        }

        ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.nick.message")
                .replace("{player}", player.getName())
                .replace("{name}", ColorUtil.color(args[0]))
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("off");
        }
        if (args.length == 2) {
            return Main.instance.getBungeeCordManager().getBukkitPlayerList();
        }
        return new ArrayList<>();
    }
}
