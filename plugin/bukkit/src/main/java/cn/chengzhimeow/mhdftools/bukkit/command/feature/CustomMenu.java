package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.folder.CustomMenuManager;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.CustomMenuUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class CustomMenu extends Command {
    public CustomMenu() {
        super(
                null,
                List.of("customMenuSettings.enable"),
                "自定义菜单",
                "mhdftools.commands.custommenu",
                true,
                ConfigSetting.getInstance().getData().getStringList("customMenuSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1) {
            sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.custommenu.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (!CustomMenuManager.getInstance().getCustomMenuIdList().contains(args[0])) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.custommenu.noMenu")
                    .replace("{menu}", args[0])
            );
            return;
        }

        CustomMenuUtil.openCustomMenu(sender, CustomMenuManager.getInstance().getCustomMenuById(args[0]));
        sender.sendMessage(LangSetting.getInstance().i18n("commands.custommenu.message")
                .replace("{menu}", args[0])
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(CustomMenuManager.getInstance().getCustomMenuIdList());
        }
        return new ArrayList<>();
    }
}
