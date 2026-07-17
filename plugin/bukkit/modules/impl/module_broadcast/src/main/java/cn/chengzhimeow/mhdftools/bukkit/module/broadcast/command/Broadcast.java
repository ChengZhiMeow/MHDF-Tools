package cn.chengzhimeow.mhdftools.bukkit.module.broadcast.command;

import cn.chengzhimeow.mhdftools.bukkit.common.message.Messager;
import cn.chengzhimeow.mhdftools.bukkit.module.broadcast.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.broadcast.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.broadcast.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.broadcast.message.BroadcastMessage;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class Broadcast extends Command {
    public Broadcast() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                LangSetting.getInstance().getConfig().commands().broadcast().description(),
                "mhdftools.commands.bed",
                true,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length == 0) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().broadcast().usage())
                    .replace("{command}", label));
            return;
        }

        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i != 0) messageBuilder.append(" ");
            messageBuilder.append(args[i]);
        }
        TextComponent message = LangSetting.getInstance().getConfig().commands().broadcast().format()
                .replace("{message}", ColorUtil.color(messageBuilder.toString()));

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().broadcast().message());
        Messager.send(
                new BroadcastMessage(
                        message.toJsonString()
                ),
                () -> {
                    Bukkit.broadcast(message);
                }
        );
    }
}
