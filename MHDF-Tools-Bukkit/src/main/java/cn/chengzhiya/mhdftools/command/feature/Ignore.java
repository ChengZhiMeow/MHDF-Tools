package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.entity.database.data.IgnoreData;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.IgnoreDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Ignore extends AbstractCommand {
    public Ignore() {
        super(
                List.of("chatSettings.ignore.enable"),
                "屏蔽",
                "mhdftools.commands.ignore",
                true,
                ConfigUtil.getConfig().getStringList("ignoreSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            // 屏蔽玩家列表
            if (args[0].equalsIgnoreCase("list")) {
                StringBuilder listStringBuilder = new StringBuilder();
                List<IgnoreData> ignoreDataList = IgnoreDataUtil.getIgnoreDataList(sender);
                for (int i = 0; i < ignoreDataList.size(); i++) {
                    IgnoreData ignoreData = ignoreDataList.get(i);
                    OfflinePlayer ignorePlayer = Bukkit.getOfflinePlayer(ignoreData.getIgnore());

                    listStringBuilder.append(ignorePlayer.getName());
                    if (i != ignoreDataList.size() - 1) {
                        listStringBuilder.append(", ");
                    }
                }

                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.ignore.subCommands.list.message")
                        .replace("{list}", !listStringBuilder.isEmpty() ? listStringBuilder.toString() : "空")
                );
                return;
            }
        }

        if (args.length == 2) {
            // 增加屏蔽玩家
            if (args[0].equals("add")) {
                if (!sender.hasPermission("mhdftools.bypass.ignore.blacklist")) {
                    if (ConfigUtil.getConfig().getStringList("ignoreSettings.blacklist").contains(args[1])) {
                        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.ignore.subCommands.add.blacklist")
                                .replace("{player}", args[1])
                        );
                        return;
                    }
                }

                OfflinePlayer ignorePlayer = Bukkit.getOfflinePlayer(args[1]);
                if (IgnoreDataUtil.isIgnore(sender, ignorePlayer)) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.ignore.subCommands.add.haveIgnore")
                            .replace("{player}", args[1])
                    );
                    return;
                }

                if (ignorePlayer.getUniqueId().equals(sender.getUniqueId())) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.ignore.subCommands.add.sendSelf"));
                    return;
                }

                IgnoreData ignoreData = new IgnoreData();
                ignoreData.setPlayer(sender.getUniqueId());
                ignoreData.setIgnore(ignorePlayer.getUniqueId());
                IgnoreDataUtil.updateIgnoreData(ignoreData);

                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.ignore.subCommands.add.message")
                        .replace("{player}", args[1])
                );
                return;
            }

            // 移除屏蔽玩家
            if (args[0].equals("remove")) {
                OfflinePlayer ignorePlayer = Bukkit.getOfflinePlayer(args[1]);
                if (!IgnoreDataUtil.isIgnore(sender, ignorePlayer)) {
                    ActionUtil.sendMessage(sender, LangUtil.i18n("commands.ignore.subCommands.remove.noIgnore")
                            .replace("{player}", args[1])
                    );
                    return;
                }

                IgnoreData ignoreData = IgnoreDataUtil.getIgnoreData(sender, ignorePlayer);
                IgnoreDataUtil.removeIgnoreData(ignoreData);

                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.ignore.subCommands.remove.message")
                        .replace("{player}", args[1])
                );
                return;
            }
        }

        // 输出帮助信息
        {
            ActionUtil.sendMessage(sender, LangUtil.i18n("commands.ignore.subCommands.help.message")
                    .replace("{helpList}", LangUtil.getHelpList("commands.ignore.subCommands"))
                    .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(LangUtil.getKeys("commands.ignore.subCommands"));
        }
        if (args.length == 2) {
            if (args[0].equals("add")) {
                return Main.instance.getBungeeCordManager().getPlayerList();
            }
            if (args[0].equals("remove")) {
                return IgnoreDataUtil.getIgnoreDataList(sender).stream()
                        .map(d -> Bukkit.getOfflinePlayer(d.getIgnore()))
                        .map(OfflinePlayer::getName)
                        .toList();
            }
        }
        return new ArrayList<>();
    }
}
