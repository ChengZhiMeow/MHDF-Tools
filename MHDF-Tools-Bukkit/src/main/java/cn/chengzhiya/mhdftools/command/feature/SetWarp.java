package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.database.data.WarpData;
import cn.chengzhiya.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhiya.mhdftools.command.Command;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class SetWarp extends Command {
    public SetWarp() {
        super(
                List.of("warpSettings.enable"),
                "设置传送点",
                "mhdftools.commands.setwarp",
                true,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("warpSettings.setwarpCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                    .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.setwarp.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        Location location = sender.getLocation();

        WarpData data = new WarpData(args[0]);
        data.setLocation(new BungeeCordLocation(location));
        MHDFToolsAPIHelper.getInstance().getWarpDataManager().update(data);

        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.setwarp.message")
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
