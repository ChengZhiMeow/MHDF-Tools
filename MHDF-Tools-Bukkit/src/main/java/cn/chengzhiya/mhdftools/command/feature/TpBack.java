package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class TpBack extends AbstractCommand {
    public TpBack() {
        super(
                List.of("tpbackSettings.enable"),
                "返回传送前的位置",
                "mhdftools.commands.tpback",
                true,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("tpbackSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                    .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.tpback.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (Main.instance.getConfigManager().getConfigManager().getData().getStringList("tpbackSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("blackWorld"));
            return;
        }

        String backLocation = Main.instance.getCacheManager().get("tpback", sender.getName());
        if (backLocation == null) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.tpback.noLocation"));
            return;
        }

        Main.instance.getBungeeCordManager().teleportLocation(sender, new BungeeCordLocation(backLocation));
        Main.instance.getBungeeCordManager().sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.tpback.message"));
    }
}
