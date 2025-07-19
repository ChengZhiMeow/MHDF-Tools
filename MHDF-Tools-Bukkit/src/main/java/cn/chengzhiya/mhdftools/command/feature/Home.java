package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.HomeData;
import cn.chengzhiya.mhdftools.command.Command;
import cn.chengzhiya.mhdftools.menu.feature.HomeMenu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Home extends Command {
    public Home() {
        super(
                List.of("homeSettings.enable"),
                "传送到指定家",
                "mhdftools.commands.home",
                true,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("homeSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (Main.instance.getConfigManager().getConfigManager().getData().getStringList("homeSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("blackWorld"));
            return;
        }

        if (args.length == 0) {
            new HomeMenu(sender, 1).openMenu();
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.home.openMenuMessage"));
            return;
        }

        if (args.length == 1) {
            MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(sender);
            if (!player.hasHome(args[0])) {
                ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.home.noHome")
                        .replace("{home}", args[0])
                );
                return;
            }

            HomeData data = player.getHome(args[0]);
            Main.instance.getBungeeCordManager().teleportLocation(sender, data.toBungeeCordLocation());
            Main.instance.getBungeeCordManager().sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.home.message")
                    .replace("{home}", args[0])
            );
            return;
        }

        // 输出帮助信息
        {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                    .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.home.usage"))
                    .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(sender);
            return player.getHomeList().stream()
                    .map(HomeData::getHome)
                    .toList();
        }
        return new ArrayList<>();
    }
}
