package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.CrashUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class Crash extends Command {
    public Crash() {
        super(
                null,
                List.of("crashSettings.enable"),
                "崩溃玩家客户端",
                "mhdftools.commands.crash",
                false,
                ConfigSetting.getSettingInstance().getData().getStringList("crashSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length == 0 || args.length >= 3) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.crash.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("playerOffline"));
            return;
        }

        String crashType = args.length == 1
                           ? ConfigSetting.getSettingInstance().getData().getString("crashSettings.defaultType")
                           : args[1];

        if (crashType != null && CrashUtil.crashPlayerClient(player, crashType)) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.crash.message")
                    .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(player).getDisplayName())
                    .replace("{type}", LangSetting.getSettingInstance().i18n("commands.crash.types." + crashType))
            );
        } else {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.crash.typeNotExists"));
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        if (args.length == 2) {
            return Arrays.asList("explosion", "posAndLook", "invalidParticle");
        }
        return new ArrayList<>();
    }
}
