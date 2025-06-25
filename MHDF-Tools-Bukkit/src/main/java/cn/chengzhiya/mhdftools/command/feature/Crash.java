package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.CrashUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Crash extends AbstractCommand {
    public Crash() {
        super(
                List.of("crashSettings.enable"),
                "崩溃玩家客户端",
                "mhdftools.commands.crash",
                false,
                ConfigUtil.getConfig().getStringList("crashSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length == 0 || args.length >= 3) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.crash.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("playerOffline"));
            return;
        }

        String crashType = args.length == 1
                ? ConfigUtil.getConfig().getString("crashSettings.defaultType")
                : args[1];

        if (crashType != null && CrashUtil.crashPlayerClient(player, crashType)) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.crash.message")
                    .replace("{type}", LangUtil.i18n("commands.crash.types." + crashType))
            );
        } else {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.crash.typeNotExists"));
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
