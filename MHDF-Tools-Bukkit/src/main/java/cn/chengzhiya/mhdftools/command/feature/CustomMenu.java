package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.feature.CustomMenuUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class CustomMenu extends AbstractCommand {
    public CustomMenu() {
        super(
                List.of("customMenuSettings.enable"),
                "自定义菜单",
                "mhdftools.commands.custommenu",
                true,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("customMenuSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                    .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.custommenu.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (!Main.instance.getConfigManager().getCustomMenuManager().getCustomMenuIdList().contains(args[0])) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.custommenu.noMenu")
                    .replace("{menu}", args[0])
            );
            return;
        }

        CustomMenuUtil.openCustomMenu(sender, Main.instance.getConfigManager().getCustomMenuManager().getCustomMenuById(args[0]));
        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.custommenu.message")
                .replace("{menu}", args[0])
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(Main.instance.getConfigManager().getCustomMenuManager().getCustomMenuIdList());
        }
        return new ArrayList<>();
    }
}
