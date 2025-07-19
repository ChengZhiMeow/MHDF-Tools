package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.database.data.WarpData;
import cn.chengzhiya.mhdftools.command.Command;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class DelWarp extends Command {
    public DelWarp() {
        super(
                List.of("warpSettings.enable"),
                "删除传送点",
                "mhdftools.commands.delwarp",
                true,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("warpSettings.delwarpCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                    .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.delwarp.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (MHDFToolsAPIHelper.getInstance().getWarpDataManager().hasData(args[0])) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.delwarp.noWarp")
                    .replace("{warp}", args[0])
            );
            return;
        }

        WarpData data = MHDFToolsAPIHelper.getInstance().getWarpDataManager().get(args[0]);
        MHDFToolsAPIHelper.getInstance().getWarpDataManager().delete(data);
        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.delwarp.message")
                .replace("{warp}", args[0])
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return MHDFToolsAPIHelper.getInstance().getWarpDataManager().getList().stream()
                    .map(WarpData::getWarp)
                    .toList();
        }
        return new ArrayList<>();
    }
}
