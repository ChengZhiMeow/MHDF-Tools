package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class Suicide extends AbstractCommand {
    public Suicide() {
        super(
                List.of("suicideSettings.enable"),
                "自杀",
                "mhdftools.commands.suicide",
                true,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("suicideSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (Main.instance.getConfigManager().getConfigManager().getData().getStringList("suicideSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("blackWorld"));
            return;
        }

        if (args.length == 0) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.suicide.confirm"));
            return;
        }

        if (args.length == 1) {
            if (args[0].equals("confirm")) {
                sender.setHealth(0.0);
                ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.suicide.message"));
                return;
            }
        }

        // 输出帮助信息
        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.suicide.usage"))
                .replace("{command}", label)
        );
    }
}
