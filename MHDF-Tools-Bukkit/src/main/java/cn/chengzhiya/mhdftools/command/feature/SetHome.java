package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.HomeData;
import cn.chengzhiya.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.feature.HomeUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class SetHome extends AbstractCommand {
    public SetHome() {
        super(
                List.of("homeSettings.enable"),
                "设置家",
                "mhdftools.commands.sethome",
                true,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("homeSettings.sethomeCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                    .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.sethome.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (Main.instance.getConfigManager().getConfigManager().getData().getStringList("homeSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("blackWorld"));
            return;
        }

        MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(sender);
        if (!Main.instance.getConfigManager().getConfigManager().getData().getBoolean("homeSettings.existReplace")) {
            if (player.hasHome(args[0])) {
                ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.sethome.haveHome")
                        .replace("{home}", args[0])
                );
                return;
            }
        }

        int maxHome = HomeUtil.getMaxHome(sender);
        if (player.getHomeList().size() >= maxHome) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.sethome.isMax")
                    .replace("{amount}", String.valueOf(maxHome))
            );
            return;
        }

        Location location = sender.getLocation();
        player.setHome(args[0], new BungeeCordLocation(location));

        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.sethome.message")
                .replace("{home}", args[0])
        );
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
