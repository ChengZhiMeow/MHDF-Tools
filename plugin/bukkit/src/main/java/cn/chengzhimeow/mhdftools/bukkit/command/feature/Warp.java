package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.database.data.WarpData;
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

final class Warp extends Command {
    public Warp() {
        super(
                null,
                List.of("warpSettings.enable"),
                "传送到指定传送点",
                "mhdftools.commands.warp",
                false,
                ConfigSetting.getInstance().getData().getStringList("warpSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = null;

        // 传送自己玩家至传送点
        if (args.length == 1 && sender instanceof Player) {
            player = (Player) sender;
        }

        // 传送其他玩家至传送点
        if (args.length >= 2) {
            if (Bukkit.getPlayer(args[1]) == null) {
                sender.sendMessage(LangSetting.getInstance().i18n("playerOffline"));
                return;
            }
            if (!sender.hasPermission("mhdftools.commands.warp.other")) {
                sender.sendMessage(LangSetting.getInstance().i18n("noPermission"));
                return;
            }
            player = Bukkit.getPlayer(args[1]);
        }

        // 输出帮助信息
        if (player == null) {
            sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.warp.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (ConfigSetting.getInstance().getData().getStringList("warpSettings.blackWorld").contains(player.getWorld().getName())) {
            sender.sendMessage(LangSetting.getInstance().i18n("blackWorld"));
            return;
        }

        if (!MHDFToolsAPI.getInstance().getWarpDataManager().hasData(args[0])) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.warp.noWarp")
                    .replace("{warp}", args[0])
            );
            return;
        }

        WarpData data = MHDFToolsAPI.getInstance().getWarpDataManager().get(args[0]);
        Main.instance.getBungeeCordManager().teleportLocation(player, data.toBungeeCordLocation());

        Main.instance.getBungeeCordManager().sendMessage(player, LangSetting.getInstance().i18n("commands.warp.message")
                .replace("{warp}", args[0])
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return MHDFToolsAPI.getInstance().getWarpDataManager().getList().stream()
                    .map(WarpData::getWarp)
                    .toList();
        }
        if (args.length == 2) {
            return null;
        }
        return new ArrayList<>();
    }
}
