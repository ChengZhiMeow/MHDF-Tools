package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.MsgUtil;
import cn.chengzhiya.mhdftools.util.message.MessageUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Msg extends AbstractCommand {
    public Msg() {
        super(
                List.of("chatSettings.enable", "chatSettings.msg.enable"),
                "私聊",
                "mhdftools.commands.msg",
                false,
                ConfigUtil.getConfig().getStringList("chatSettings.msg.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length < 2) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.msg.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (!Main.instance.getBungeeCordManager().ifPlayerOnline(args[0])) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("playerOffline"));
            return;
        }

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(args[0]);
        if (mhdfPlayer.isEnableVanish()) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("playerOffline"));
        }

        String message = MessageUtil.mergeString(args, 1);
        MsgUtil.sendMsg(sender, args[0], message);
    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        return new ArrayList<>();
    }
}
