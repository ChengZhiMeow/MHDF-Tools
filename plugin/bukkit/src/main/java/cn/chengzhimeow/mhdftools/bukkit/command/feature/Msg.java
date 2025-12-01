package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.MsgUtil;
import cn.chengzhimeow.mhdftools.message.StringUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Msg extends Command {
    public Msg() {
        super(
                null,
                List.of("chatSettings.enable", "chatSettings.msg.enable"),
                "私聊",
                "mhdftools.commands.msg",
                false,
                ConfigSetting.getInstance().getData().getStringList("chatSettings.msg.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length < 2) {
            sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.msg.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (!Main.instance.getBungeeCordManager().ifPlayerOnline(args[0])) {
            sender.sendMessage(LangSetting.getInstance().i18n("playerOffline"));
            return;
        }

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(args[0]);
        if (mhdfPlayer.isEnableVanish()) {
            sender.sendMessage(LangSetting.getInstance().i18n("playerOffline"));
        }

        String message = StringUtil.join(args, 1);
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
