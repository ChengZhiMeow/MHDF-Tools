package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.menu.feature.chat.LookItemMenu;
import cn.chengzhiya.mhdftools.util.Base64Util;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class LookItem extends AbstractCommand {
    public LookItem() {
        super(
                List.of("chatSettings.enable", "chatSettings.showItem.enable"),
                "展示物品",
                "mhdftools.commands.lookitem",
                true,
                ConfigUtil.getConfig().getStringList("chatSettings.showItem.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.lookitem.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        String data = Main.instance.getCacheManager().get("showItem", args[0]);
        if (data == null) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.lookitem.noData"));
            return;
        }

        new LookItemMenu(sender, Base64Util.decode(data)).openMenu();
        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.lookitem.message"));
    }
}
