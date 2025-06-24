package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.location.BungeeCordLocation;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TpBack extends AbstractCommand {
    public TpBack() {
        super(
                List.of("tpbackSettings.enable"),
                "返回传送前的位置",
                "mhdftools.commands.tpback",
                true,
                ConfigUtil.getConfig().getStringList("tpbackSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.tpback.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (ConfigUtil.getConfig().getStringList("tpbackSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("blackWorld"));
            return;
        }

        String backLocationBase64 = Main.instance.getCacheManager().get("tpback", sender.getName());
        if (backLocationBase64 == null) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.tpback.noLocation"));
            return;
        }

        Main.instance.getBungeeCordManager().teleportLocation(sender, new BungeeCordLocation(backLocationBase64));
        Main.instance.getBungeeCordManager().sendMessage(sender, LangUtil.i18n("commands.tpback.message"));
    }
}
