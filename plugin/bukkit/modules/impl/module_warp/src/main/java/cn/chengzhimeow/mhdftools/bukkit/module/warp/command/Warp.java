package cn.chengzhimeow.mhdftools.bukkit.module.warp.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.WarpData;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.warp.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.warp.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.warp.config.LangSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Warp extends Command {
    public Warp() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                LangSetting.getInstance().getConfig().commands().warp().description(),
                "mhdftools.commands.warp",
                false,
                ConfigSetting.getInstance().getConfig().warpCommands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player target = null;
        MHDFToolsPlayer targetMhdfPlayer = null;

        if (args.length == 1 && sender instanceof Player player) {
            target = player;
            targetMhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
        }

        if (args.length == 2) {
            if (!sender.hasPermission("mhdftools.commands.warp.other")) {
                sender.sendMessage(GlobalLangSetting.getInstance().getConfig().noPermission());
                return;
            }

            target = org.bukkit.Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
                return;
            }
            targetMhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(target.getUniqueId(), target.getName());
        }

        if (targetMhdfPlayer == null) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().warp().usage())
                    .replace("{command}", label));
            return;
        }

        if (ConfigSetting.getInstance().getConfig().blackWorld().contains(target.getWorld().getName())) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().blackWorld());
            return;
        }

        if (!MHDFToolsAPI.getInstance().getWarpDataManager().hasData(args[0])) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().warp().noWarp()
                    .replace("{warp}", args[0]));
            return;
        }

        targetMhdfPlayer.teleport(MHDFToolsAPI.getInstance().getWarpDataManager().get(args[0]).toBungeeCordLocation());
        targetMhdfPlayer.sendMessage(LangSetting.getInstance().getConfig().commands().warp().message()
                .replace("{warp}", args[0]));
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return MHDFToolsAPI.getInstance().getWarpDataManager().getList().stream()
                    .map(WarpData::getWarp)
                    .toList();
        }
        if (args.length == 2 && sender.hasPermission("mhdftools.commands.warp.other")) {
            return BungeeCordManager.getInstance().getBukkitPlayerList();
        }
        return new ArrayList<>();
    }
}
