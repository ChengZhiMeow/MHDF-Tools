package cn.chengzhimeow.mhdftools.bukkit.module.warp.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.database.data.WarpData;
import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkitAdapt;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.warp.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.warp.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.warp.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class SetWarp extends Command {
    public SetWarp() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                LangSetting.getInstance().getConfig().commands().setwarp().description(),
                "mhdftools.commands.setwarp",
                true,
                ConfigSetting.getInstance().getConfig().setWarpCommands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().setwarp().usage())
                    .replace("{command}", label));
            return;
        }

        Location location = sender.getLocation();
        WarpData data = new WarpData(args[0], new BungeeCordLocation(MHDFToolsBukkitAdapt.adapt(location)));
        MHDFToolsAPI.getInstance().getWarpDataManager().update(data);

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().setwarp().message()
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
