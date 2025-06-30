package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Suicide extends AbstractCommand {
    public Suicide() {
        super(
                List.of("suicideSettings.enable"),
                "自杀",
                "mhdftools.commands.suicide",
                true,
                ConfigUtil.getConfig().getStringList("suicideSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (ConfigUtil.getConfig().getStringList("suicideSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("blackWorld"));
            return;
        }

        if (args.length == 0) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.suicide.confirm"));
            return;
        }

        if (args.length == 1) {
            if (args[0].equals("confirm")) {
                sender.setHealth(0.0);
                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.suicide.message"));
                return;
            }
        }

        // 输出帮助信息
        ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                .replace("{usage}", LangUtil.i18n("commands.suicide.usage"))
                .replace("{command}", label)
        );
    }
}
