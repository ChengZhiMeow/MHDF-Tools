package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.IgnoreData;
import cn.chengzhiya.mhdftools.command.Command;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Ignore extends Command {
    public Ignore() {
        super(
                List.of("chatSettings.ignore.enable"),
                "屏蔽",
                "mhdftools.commands.ignore",
                true,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("ignoreSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(sender);
        if (args.length == 1) {
            // 屏蔽玩家列表
            if (args[0].equalsIgnoreCase("list")) {
                StringBuilder listStringBuilder = new StringBuilder();
                List<IgnoreData> ignoreDataList = mhdfPlayer.getIgnoreList();
                for (int i = 0; i < ignoreDataList.size(); i++) {
                    IgnoreData ignoreData = ignoreDataList.get(i);
                    OfflinePlayer ignorePlayer = Bukkit.getOfflinePlayer(ignoreData.getIgnore());

                    listStringBuilder.append(ignorePlayer.getName());
                    if (i != ignoreDataList.size() - 1) {
                        listStringBuilder.append(", ");
                    }
                }

                ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.ignore.subCommands.list.message")
                        .replace("{list}", !listStringBuilder.isEmpty() ? listStringBuilder.toString() : "空")
                );
                return;
            }
        }

        if (args.length == 2) {
            // 增加屏蔽玩家
            if (args[0].equals("add")) {
                if (!sender.hasPermission("mhdftools.bypass.ignore.blacklist")) {
                    if (Main.instance.getConfigManager().getConfigManager().getData().getStringList("ignoreSettings.blacklist").contains(args[1])) {
                        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.ignore.subCommands.add.blacklist")
                                .replace("{player}", args[1])
                        );
                        return;
                    }
                }

                MHDFToolsPlayer mhdfIgnorePlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(args[1]);
                if (mhdfPlayer.isIgnore(mhdfIgnorePlayer)) {
                    ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.ignore.subCommands.add.haveIgnore")
                            .replace("{player}", args[1])
                    );
                    return;
                }

                if (mhdfPlayer.equals(mhdfIgnorePlayer)) {
                    ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.ignore.subCommands.add.sendSelf"));
                    return;
                }

                mhdfPlayer.ignore(mhdfIgnorePlayer);

                ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.ignore.subCommands.add.message")
                        .replace("{player}", args[1])
                );
                return;
            }

            // 移除屏蔽玩家
            if (args[0].equals("remove")) {
                MHDFToolsPlayer mhdfIgnorePlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(args[1]);

                if (!mhdfPlayer.isIgnore(mhdfIgnorePlayer)) {
                    ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.ignore.subCommands.remove.noIgnore")
                            .replace("{player}", args[1])
                    );
                    return;
                }

                mhdfPlayer.deleteIgnore(mhdfIgnorePlayer);

                ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.ignore.subCommands.remove.message")
                        .replace("{player}", args[1])
                );
                return;
            }
        }

        // 输出帮助信息
        {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.ignore.subCommands.help.message")
                    .replace("{helpList}", Main.instance.getConfigManager().getLangManager().getHelpList("commands.ignore.subCommands"))
                    .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(Main.instance.getConfigManager().getLangManager().getKeys("commands.ignore.subCommands"));
        }
        if (args.length == 2) {
            if (args[0].equals("add")) {
                return Main.instance.getBungeeCordManager().getPlayerList();
            }
            if (args[0].equals("remove")) {
                MHDFToolsPlayer mhdfPlayer = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(sender);
                return mhdfPlayer.getIgnoreList().stream()
                        .map(d -> Bukkit.getOfflinePlayer(d.getIgnore()))
                        .map(OfflinePlayer::getName)
                        .toList();
            }
        }
        return new ArrayList<>();
    }
}
