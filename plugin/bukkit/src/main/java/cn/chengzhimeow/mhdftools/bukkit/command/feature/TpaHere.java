package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.enums.TeleportRequestType;
import cn.chengzhimeow.mhdftools.bukkit.menu.feature.TeleportRequestMenu;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.TpaHereUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class TpaHere extends Command {
    public TpaHere() {
        super(
                null,
                List.of("tpahereSettings.enable"),
                "请求指定玩家传送到当前位置",
                "mhdftools.commands.tpahere",
                true,
                ConfigSetting.getSettingInstance().getData().getStringList("tpahereSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (ConfigSetting.getSettingInstance().getData().getStringList("tpahereSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("blackWorld"));
            return;
        }

        if (args.length == 0) {
            new TeleportRequestMenu(sender, TeleportRequestType.TPAHERE, 1).openMenu();
            return;
        }
        if (args.length == 1) {
            TpaHereUtil.sendTpaHereRequest(sender, args[0]);
            return;
        }
        if (args.length == 2) {
            String targetPlayerName = Main.instance.getCacheManager().get("tpaherePlayer", args[1]);
            if (targetPlayerName == null) {
                ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.tpahere.noRequest")
                        .replace("{player}", args[1])
                );
                return;
            }

            if (!targetPlayerName.equals(sender.getName())) {
                ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.tpahere.noRequest")
                        .replace("{player}", args[1])
                );
                return;
            }

            Main.instance.getCacheManager().remove("tpaherePlayer", args[1]);
            Main.instance.getCacheManager().remove("tpahereDelay", args[1]);

            if (!Main.instance.getBungeeCordManager().ifPlayerOnline(args[1])) {
                ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("playerOffline"));
                return;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            switch (args[0]) {
                case "accept" -> {
                    Main.instance.getBungeeCordManager().teleportPlayer(sender, args[1]);
                    Main.instance.getBungeeCordManager().sendMessage(args[1], LangSetting.getSettingInstance().i18n("commands.tpahere.accept.accepted")
                            .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(sender).getDisplayName())
                    );

                    ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.tpahere.accept.message")
                            .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(target).getDisplayName())
                    );
                    return;
                }
                case "reject" -> {
                    Main.instance.getBungeeCordManager().sendMessage(args[1], LangSetting.getSettingInstance().i18n("commands.tpahere.reject.rejected")
                            .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(sender).getDisplayName())
                    );

                    ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.tpahere.reject.message")
                            .replace("{player}", MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(target).getDisplayName())
                    );
                    return;
                }
            }
        }

        {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.tpahere.usage"))
                    .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        return new ArrayList<>();
    }
}