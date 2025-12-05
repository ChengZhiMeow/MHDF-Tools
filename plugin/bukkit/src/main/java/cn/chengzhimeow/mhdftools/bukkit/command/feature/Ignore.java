package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.IgnoreData;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Ignore extends Command {
    public Ignore() {
        super(
                null,
                List.of("ignoreSettings.enable"),
                "屏蔽",
                "mhdftools.commands.ignore",
                true,
                ConfigSetting.getInstance().getData().getStringList("ignoreSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender);
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

                sender.sendMessage(LangSetting.getInstance().i18n("commands.ignore.subCommands.list.message")
                        .replace("{list}", !listStringBuilder.isEmpty() ? listStringBuilder.toString() : "空")
                );
                return;
            }
        }

        if (args.length == 2) {
            // 增加屏蔽玩家
            if (args[0].equals("add")) {
                if (!sender.hasPermission("mhdftools.bypass.ignore.blacklist")) {
                    if (ConfigSetting.getInstance().getData().getStringList("ignoreSettings.blacklist").contains(args[1])) {
                        sender.sendMessage(LangSetting.getInstance().i18n("commands.ignore.subCommands.add.blacklist")
                                .replace("{player}", args[1])
                        );
                        return;
                    }
                }

                MHDFToolsPlayer mhdfIgnorePlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(args[1]);
                if (mhdfPlayer.isIgnore(mhdfIgnorePlayer)) {
                    sender.sendMessage(LangSetting.getInstance().i18n("commands.ignore.subCommands.add.haveIgnore")
                            .replace("{player}", args[1])
                    );
                    return;
                }

                if (mhdfPlayer.equals(mhdfIgnorePlayer)) {
                    sender.sendMessage(LangSetting.getInstance().i18n("commands.ignore.subCommands.add.sendSelf"));
                    return;
                }

                mhdfPlayer.ignore(mhdfIgnorePlayer);

                sender.sendMessage(LangSetting.getInstance().i18n("commands.ignore.subCommands.add.message")
                        .replace("{player}", args[1])
                );
                return;
            }

            // 移除屏蔽玩家
            if (args[0].equals("remove")) {
                MHDFToolsPlayer mhdfIgnorePlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(args[1]);

                if (!mhdfPlayer.isIgnore(mhdfIgnorePlayer)) {
                    sender.sendMessage(LangSetting.getInstance().i18n("commands.ignore.subCommands.remove.noIgnore")
                            .replace("{player}", args[1])
                    );
                    return;
                }

                mhdfPlayer.deleteIgnore(mhdfIgnorePlayer);

                sender.sendMessage(LangSetting.getInstance().i18n("commands.ignore.subCommands.remove.message")
                        .replace("{player}", args[1])
                );
                return;
            }
        }

        // 输出帮助信息
        {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.ignore.subCommands.help.message")
                    .replace("{help_list}", LangSetting.getInstance().getHelpList("commands.ignore.subCommands"))
                    .replace("{command}", label)
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(LangSetting.getInstance().getKeys("commands.ignore.subCommands"));
        }
        if (args.length == 2) {
            if (args[0].equals("add")) {
                return Main.instance.getBungeeCordManager().getPlayerList();
            }
            if (args[0].equals("remove")) {
                MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender);
                return mhdfPlayer.getIgnoreList().stream()
                        .map(d -> Bukkit.getOfflinePlayer(d.getIgnore()))
                        .map(OfflinePlayer::getName)
                        .toList();
            }
        }
        return new ArrayList<>();
    }
}
