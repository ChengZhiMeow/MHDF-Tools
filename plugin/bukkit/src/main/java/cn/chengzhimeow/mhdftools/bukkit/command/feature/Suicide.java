package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class Suicide extends Command {
    public Suicide() {
        super(
                null,
                List.of("suicideSettings.enable"),
                "自杀",
                "mhdftools.commands.suicide",
                true,
                ConfigSetting.getInstance().getData().getStringList("suicideSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (ConfigSetting.getInstance().getData().getStringList("suicideSettings.blackWorld").contains(sender.getWorld().getName())) {
            sender.sendMessage(LangSetting.getInstance().i18n("blackWorld"));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.suicide.confirm"));
            return;
        }

        if (args.length == 1) {
            if (args[0].equals("confirm")) {
                sender.setHealth(0.0);
                sender.sendMessage(LangSetting.getInstance().i18n("commands.suicide.message"));
                return;
            }
        }

        // 输出帮助信息
        sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                .replace("{usage}", LangSetting.getInstance().i18n("commands.suicide.usage"))
                .replace("{command}", label)
        );
    }
}
