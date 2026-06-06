package cn.chengzhimeow.mhdftools.bukkit.module.chat.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.service.PrivateMessageService;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.message.StringUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

final class Reply extends Command {
    public Reply() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable() && ConfigSetting.getInstance().getConfig().msg().enable(),
                "回复私聊",
                "mhdftools.commands.reply",
                false,
                ConfigSetting.getInstance().getConfig().msg().replyCommands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().reply().usage())
                    .replace("{command}", label));
            return;
        }

        String replyTarget = PrivateMessageService.replyTarget(sender.getName());
        if (replyTarget == null) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().reply().noTarget());
            return;
        }

        if (!BungeeCordManager.getInstance().ifPlayerOnline(replyTarget)) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
            return;
        }

        MHDFToolsPlayer target = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(replyTarget);
        if (target.isEnableVanish()) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().playerOffline());
            return;
        }

        PrivateMessageService.sendMsg(sender, replyTarget, StringUtil.join(args, 0));
    }
}
