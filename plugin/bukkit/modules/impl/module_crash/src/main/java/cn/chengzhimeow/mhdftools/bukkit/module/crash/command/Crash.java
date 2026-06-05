package cn.chengzhimeow.mhdftools.bukkit.module.crash.command;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkitAdapt;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.crash.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.crash.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.crash.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.crash.util.CrashUtil;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

final class Crash extends Command {
    public Crash() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "崩溃玩家客户端",
                "mhdftools.commands.crash",
                false,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1 && args.length != 2 && args.length != 3) {
            sender.sendMessage(GlobalLangSetting.getInstance().i18n("usage_error")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.crash.usage"))
                    .replace("{command}", label));
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(GlobalLangSetting.getInstance().i18n("player_offline"));
            return;
        }

        String type = args.length == 1
                      ? ConfigSetting.getInstance().getConfig().defaultType()
                      : args[1].toLowerCase();

        if (type == null || !CrashUtil.crashPlayerClient(player, type)) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.crash.no_type"));
            return;
        }

        sender.sendMessage(LangSetting.getInstance().i18n("commands.crash.message")
                .replace("{player}", MHDFToolsBukkitAdapt.adapt(player).getDisplayName())
                .replace("{type}", LangSetting.getInstance().i18n("commands.crash.types." + type)));
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return BungeeCordManager.getInstance().getBukkitPlayerList();
        if (args.length == 2) return Arrays.asList("explosion", "invalid_teleport", "invalid_particle");
        return super.tabCompleter(sender, label, args);
    }
}

