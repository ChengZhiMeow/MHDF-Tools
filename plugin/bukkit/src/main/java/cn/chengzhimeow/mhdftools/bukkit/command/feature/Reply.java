package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.MsgUtil;
import cn.chengzhimeow.mhdftools.message.StringUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class Reply extends Command {
    public Reply() {
        super(
                null,
                List.of("chatSettings.enable", "chatSettings.msg.enable"),
                "回复私聊",
                "mhdftools.commands.reply",
                false,
                ConfigSetting.getInstance().getData().getStringList("chatSettings.msg.replyCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length < 1) {
            sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.reply.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        String replyTarget = Main.instance.getCacheManager().get("reply", sender.getName());
        if (replyTarget == null) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.reply.noTarget"));
            return;
        }

        if (!Main.instance.getBungeeCordManager().ifPlayerOnline(replyTarget)) {
            sender.sendMessage(LangSetting.getInstance().i18n("playerOffline"));
            return;
        }

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(replyTarget);
        if (mhdfPlayer.isEnableVanish()) {
            sender.sendMessage(LangSetting.getInstance().i18n("playerOffline"));
        }

        String message = StringUtil.join(args, 0);
        MsgUtil.sendMsg(sender, replyTarget, message);
    }
}
