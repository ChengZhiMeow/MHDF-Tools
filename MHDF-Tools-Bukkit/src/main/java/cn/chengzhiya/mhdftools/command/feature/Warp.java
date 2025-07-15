package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.database.data.WarpData;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Warp extends AbstractCommand {
    public Warp() {
        super(
                List.of("warpSettings.enable"),
                "传送到指定传送点",
                "mhdftools.commands.warp",
                false,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("warpSettings.commands").toArray(new String[0])
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
                ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("playerOffline"));
                return;
            }
            if (!sender.hasPermission("mhdftools.commands.warp.other")) {
                ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("noPermission"));
                return;
            }
            player = Bukkit.getPlayer(args[1]);
        }

        // 输出帮助信息
        if (player == null) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                    .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.warp.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (Main.instance.getConfigManager().getConfigManager().getData().getStringList("warpSettings.blackWorld").contains(player.getWorld().getName())) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("blackWorld"));
            return;
        }

        if (!MHDFToolsAPIHelper.getInstance().getWarpDataManager().hasData(args[0])) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.warp.noWarp")
                    .replace("{warp}", args[0])
            );
            return;
        }

        WarpData data = MHDFToolsAPIHelper.getInstance().getWarpDataManager().get(args[0]);
        Main.instance.getBungeeCordManager().teleportLocation(player, data.toBungeeCordLocation());

        Main.instance.getBungeeCordManager().sendMessage(player, Main.instance.getConfigManager().getLangManager().i18n("commands.warp.message")
                .replace("{warp}", args[0])
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return MHDFToolsAPIHelper.getInstance().getWarpDataManager().getList().stream()
                    .map(WarpData::getWarp)
                    .toList();
        }
        if (args.length == 2) {
            return null;
        }
        return new ArrayList<>();
    }
}
