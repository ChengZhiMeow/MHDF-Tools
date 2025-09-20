package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.command.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.MsgUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.message.MessageUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class Reply extends Command {
    public Reply() {
        super(
                List.of("chatSettings.enable", "chatSettings.msg.enable"),
                "回复私聊",
                "mhdftools.commands.reply",
                false,
                ConfigSetting.getSettingInstance().getData().getStringList("chatSettings.msg.replyCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length < 1) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.reply.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        String replyTarget = Main.instance.getCacheManager().get("reply", sender.getName());
        if (replyTarget == null) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.reply.noTarget"));
            return;
        }

        if (!Main.instance.getBungeeCordManager().ifPlayerOnline(replyTarget)) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("playerOffline"));
            return;
        }

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(replyTarget);
        if (mhdfPlayer.isEnableVanish()) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("playerOffline"));
        }

        String message = MessageUtil.mergeString(args, 0);
        MsgUtil.sendMsg(sender, replyTarget, message);
    }
}
