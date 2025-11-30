package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.HomeData;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.menu.feature.HomeMenu;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Home extends Command {
    public Home() {
        super(
                null,
                List.of("homeSettings.enable"),
                "传送到指定家",
                "mhdftools.commands.home",
                true,
                ConfigSetting.getSettingInstance().getData().getStringList("homeSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (ConfigSetting.getSettingInstance().getData().getStringList("homeSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("blackWorld"));
            return;
        }

        if (args.length == 0) {
            new HomeMenu(sender, 1).openMenu();
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.home.openMenuMessage"));
            return;
        }

        if (args.length == 1) {
            String regex = ConfigSetting.getSettingInstance().getData().getString("homeSettings.regex");
            if (regex != null && !args[0].matches(regex)) {
                ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.home.invalidName"));
                return;
            }

            MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(sender);
            if (!player.hasHome(args[0])) {
                ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.home.noHome")
                        .replace("{home}", args[0])
                );
                return;
            }

            HomeData data = player.getHome(args[0]);
            Main.instance.getBungeeCordManager().teleportLocation(sender, data.toBungeeCordLocation());
            Main.instance.getBungeeCordManager().sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.home.message")
                    .replace("{home}", args[0])
            );
            return;
        }

        // 输出帮助信息
        {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.home.usage"))
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
