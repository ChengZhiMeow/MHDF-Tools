package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.menu.feature.chat.LookItemMenu;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.util.Base64Util;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class LookItem extends Command {
    public LookItem() {
        super(
                null,
                List.of("chatSettings.enable", "chatSettings.showItem.enable"),
                "展示物品",
                "mhdftools.commands.lookitem",
                true,
                ConfigSetting.getInstance().getData().getStringList("chatSettings.showItem.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.lookitem.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        String data = Main.instance.getCacheManager().get("showItem", args[0]);
        if (data == null) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.lookitem.noData"));
            return;
        }

        new LookItemMenu(sender, Base64Util.decode(data)).openMenu();
        sender.sendMessage(LangSetting.getInstance().i18n("commands.lookitem.message"));
    }
}
