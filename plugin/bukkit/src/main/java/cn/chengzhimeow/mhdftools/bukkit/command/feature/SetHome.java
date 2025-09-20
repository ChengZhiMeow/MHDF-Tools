package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.HomeData;
import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhimeow.mhdftools.bukkit.command.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.HomeUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class SetHome extends Command {
    public SetHome() {
        super(
                List.of("homeSettings.enable"),
                "设置家",
                "mhdftools.commands.sethome",
                true,
                ConfigSetting.getSettingInstance().getData().getStringList("homeSettings.sethomeCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 1) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.sethome.usage"))
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
        if (!ConfigSetting.getSettingInstance().getData().getBoolean("homeSettings.existReplace")) {
            if (player.hasHome(args[0])) {
                ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.sethome.haveHome")
                        .replace("{home}", args[0])
                );
                return;
            }
        }

        int maxHome = HomeUtil.getMaxHome(sender);
        if (player.getHomeList().size() >= maxHome) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.sethome.isMax")
                    .replace("{amount}", String.valueOf(maxHome))
            );
            return;
        }

        Location location = sender.getLocation();
        player.setHome(args[0], new BungeeCordLocation(location));

        ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.sethome.message")
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
