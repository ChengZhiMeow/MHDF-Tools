package cn.chengzhimeow.mhdftools.bukkit.module.warp.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.database.data.WarpData;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.warp.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.warp.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.warp.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class DelWarp extends Command {
    public DelWarp() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                LangSetting.getInstance().getConfig().commands().delwarp().description(),
                "mhdftools.commands.delwarp",
                true,
                ConfigSetting.getInstance().getConfig().delWarpCommands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().delwarp().usage())
                    .replace("{command}", label));
            return;
        }

        if (!MHDFToolsAPI.getInstance().getWarpDataManager().hasData(args[0])) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().delwarp().noWarp()
                    .replace("{warp}", args[0]));
            return;
        }

        WarpData data = MHDFToolsAPI.getInstance().getWarpDataManager().get(args[0]);
        MHDFToolsAPI.getInstance().getWarpDataManager().delete(data);
        sender.sendMessage(LangSetting.getInstance().getConfig().commands().delwarp().message()
                .replace("{warp}", args[0]));
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return MHDFToolsAPI.getInstance().getWarpDataManager().getList().stream()
                    .map(WarpData::getWarp)
                    .toList();
        }
        return new ArrayList<>();
    }
}
