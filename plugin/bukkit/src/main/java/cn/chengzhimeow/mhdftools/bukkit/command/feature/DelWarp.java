package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.database.data.WarpData;
import cn.chengzhimeow.mhdftools.bukkit.command.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
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
                ConfigSetting.getSettingInstance().getData().getStringList("warpSettings.delwarpCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.delwarp.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (MHDFToolsAPIHelper.getInstance().getWarpDataManager().hasData(args[0])) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.delwarp.noWarp")
                    .replace("{warp}", args[0])
            );
            return;
        }

        WarpData data = MHDFToolsAPIHelper.getInstance().getWarpDataManager().get(args[0]);
        MHDFToolsAPIHelper.getInstance().getWarpDataManager().delete(data);
        ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.delwarp.message")
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
