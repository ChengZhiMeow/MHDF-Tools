package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.database.data.HomeData;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.HomeDataUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class DelHome extends AbstractCommand {
    public DelHome() {
        super(
                List.of("homeSettings.enable"),
                "删除家",
                "mhdftools.commands.delhome",
                true,
                ConfigUtil.getConfig().getStringList("homeSettings.delhomeCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.delhome.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (ConfigUtil.getConfig().getStringList("homeSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("blackWorld"));
            return;
        }

        HomeData homeData = HomeDataUtil.getHomeData(sender, args[0]);
        if (homeData == null) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.delhome.noHome")
                    .replace("{home}", args[0])
            );
            return;
        }

        HomeDataUtil.removeHomeData(homeData);
        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.delhome.message")
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

