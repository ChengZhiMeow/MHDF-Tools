package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.enums.TeleportRequestType;
import cn.chengzhiya.mhdftools.menu.feature.TeleportRequestMenu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.NickUtil;
import cn.chengzhiya.mhdftools.util.feature.TpaUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Tpa extends AbstractCommand {
    public Tpa() {
        super(
                List.of("tpaSettings.enable"),
                "请求传送到指定玩家位置",
                "mhdftools.commands.tpa",
                true,
                ConfigUtil.getConfig().getStringList("tpaSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (ConfigUtil.getConfig().getStringList("tpaSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("blackWorld"));
            return;
        }

        if (args.length == 0) {
            new TeleportRequestMenu(sender, TeleportRequestType.TPA, 1).openMenu();
            return;
        }
        if (args.length == 1) {
            TpaUtil.sendTpaRequest(sender, args[0]);
            return;
        }
        if (args.length == 2) {
            String targetPlayerName = Main.instance.getCacheManager().get("tpaPlayer", args[1]);
            if (targetPlayerName == null) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.tpa.noRequest"));
                return;
            }

            if (!targetPlayerName.equals(sender.getName())) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.tpa.noRequest"));
                return;
            }

            Main.instance.getCacheManager().remove("tpaPlayer", args[1]);
            Main.instance.getCacheManager().remove("tpaDelay", args[1]);

            if (!Main.instance.getBungeeCordManager().ifPlayerOnline(args[1])) {
                ActionUtil.sendMessage(sender, LangUtil.i18n("playerOffline"));
                return;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            switch (args[0]) {
                case "accept" -> {
                    Main.instance.getBungeeCordManager().teleportPlayer(args[1], sender);
                    Main.instance.getBungeeCordManager().sendMessage(args[1], LangUtil.i18n("commands.tpa.accept.accepted")
                            .replace("{player}", NickUtil.getName(sender))
                    );

                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.tpa.accept.message")
                            .replace("{player}", NickUtil.getName(target))
                    );
                    return;
                }
                case "reject" -> {
                    Main.instance.getBungeeCordManager().sendMessage(args[1], LangUtil.i18n("commands.tpa.reject.rejected")
                            .replace("{player}", NickUtil.getName(sender))
                    );

                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.tpa.reject.message")
                            .replace("{player}", NickUtil.getName(target))
                    );
                    return;
                }
            }
        }

        {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.tpa.usage"))
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