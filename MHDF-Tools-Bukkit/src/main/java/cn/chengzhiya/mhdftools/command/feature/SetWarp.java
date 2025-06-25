package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.database.data.WarpData;
import cn.chengzhiya.mhdftools.entity.location.BungeeCordLocation;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.WarpDataUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class SetWarp extends AbstractCommand {
    public SetWarp() {
        super(
                List.of("warpSettings.enable"),
                "设置传送点",
                "mhdftools.commands.setwarp",
                true,
                ConfigUtil.getConfig().getStringList("warpSettings.setwarpCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.setwarp.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        Location location = sender.getLocation();

        WarpData warpData = new WarpData();
        warpData.setWarp(args[0]);
        warpData.setLocation(new BungeeCordLocation(location));

        WarpDataUtil.updateWarpData(warpData);
        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.setwarp.message")
                .replace("{warp}", args[0])
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return WarpDataUtil.getHomeDataList().stream()
                    .map(WarpData::getWarp)
                    .toList();
        }
        return new ArrayList<>();
    }
}
