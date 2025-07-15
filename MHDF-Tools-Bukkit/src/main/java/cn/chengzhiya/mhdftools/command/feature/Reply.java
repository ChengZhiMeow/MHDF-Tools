package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.feature.MsgUtil;
import cn.chengzhiya.mhdftools.util.message.MessageUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class Reply extends AbstractCommand {
    public Reply() {
        super(
                List.of("chatSettings.enable", "chatSettings.msg.enable"),
                "回复私聊",
                "mhdftools.commands.reply",
                false,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("chatSettings.msg.replyCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length < 1) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                    .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.reply.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        String replyTarget = Main.instance.getCacheManager().get("reply", sender.getName());
        if (replyTarget == null) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.reply.noTarget"));
            return;
        }

        if (!Main.instance.getBungeeCordManager().ifPlayerOnline(replyTarget)) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("playerOffline"));
            return;
        }

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(replyTarget);
        if (mhdfPlayer.isEnableVanish()) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("playerOffline"));
        }

        String message = MessageUtil.mergeString(args, 0);
        MsgUtil.sendMsg(sender, replyTarget, message);
    }
}
