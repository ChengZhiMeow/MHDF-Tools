package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.database.data.HomeData;
import cn.chengzhiya.mhdftools.entity.location.BungeeCordLocation;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.HomeDataUtil;
import cn.chengzhiya.mhdftools.util.feature.HomeUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class SetHome extends AbstractCommand {
    public SetHome() {
        super(
                List.of("homeSettings.enable"),
                "设置家",
                "mhdftools.commands.sethome",
                true,
                ConfigUtil.getConfig().getStringList("homeSettings.sethomeCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.sethome.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (ConfigUtil.getConfig().getStringList("homeSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("blackWorld"));
            return;
        }

        if (!ConfigUtil.getConfig().getBoolean("homeSettings.existReplace")) {
            if (HomeDataUtil.getHomeData(sender.getUniqueId(), args[0]) != null) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.sethome.haveHome")
                        .replace("{home}", args[0])
                );
                return;
            }
        }

        int maxHome = HomeUtil.getMaxHome(sender);
        if (HomeDataUtil.getHomeDataList(sender).size() >= maxHome) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.sethome.isMax")
                    .replace("{amount}", String.valueOf(maxHome))
            );
            return;
        }

        Location location = sender.getLocation();

        HomeData homeData = new HomeData();
        homeData.setPlayer(sender.getUniqueId());
        homeData.setHome(args[0]);
        homeData.setLocation(new BungeeCordLocation(location));

        HomeDataUtil.updateHomeData(homeData);
        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.sethome.message")
                .replace("{home}", args[0])
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return HomeDataUtil.getHomeDataList(sender).stream()
                    .map(HomeData::getHome)
                    .toList();
        }
        return new ArrayList<>();
    }
}
