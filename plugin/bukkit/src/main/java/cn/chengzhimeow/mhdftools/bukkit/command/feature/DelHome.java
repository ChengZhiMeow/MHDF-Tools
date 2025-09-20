package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.HomeData;
import cn.chengzhimeow.mhdftools.bukkit.command.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class DelHome extends Command {
    public DelHome() {
        super(
                List.of("homeSettings.enable"),
                "删除家",
                "mhdftools.commands.delhome",
                true,
                ConfigSetting.getSettingInstance().getData().getStringList("homeSettings.delhomeCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.delhome.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (ConfigSetting.getSettingInstance().getData().getStringList("homeSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("blackWorld"));
            return;
        }

        String regex = ConfigSetting.getSettingInstance().getData().getString("homeSettings.regex");
        if (regex != null && !args[0].matches(regex)) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.home.invalidName"));
            return;
        }

        MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(sender);
        if (!player.hasHome(args[0])) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.delhome.noHome")
                    .replace("{home}", args[0])
            );
            return;
        }

        player.deleteHome(args[0]);
        ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.delhome.message")
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

