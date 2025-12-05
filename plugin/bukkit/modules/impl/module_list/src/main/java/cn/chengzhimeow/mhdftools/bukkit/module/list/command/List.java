package cn.chengzhimeow.mhdftools.bukkit.module.list.command;

import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.list.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.list.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.list.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.list.util.ListUtil;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

final class List extends Command {
    public List() {
        super(
                ModuleMain.instance,
                java.util.List.of("enable"),
                "查看在线列表",
                "mhdftools.commands.list",
                false,
                ConfigSetting.getInstance().getData().getStringList("commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            sender.sendMessage(GlobalLangSetting.getInstance().i18n("usage_error")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.list.usage"))
                    .replace("{command}", label));
            return;
        }

        boolean getPlayerListFromBungeeCord = ConfigSetting.getInstance().getData().getBoolean("get_player_list_from_bungeecord");
        java.util.List<String> playerList = getPlayerListFromBungeeCord
                                            ? BungeeCordManager.getInstance().getPlayerList()
                                            : BungeeCordManager.getInstance().getBukkitPlayerList();

        sender.sendMessage(LangSetting.getInstance().i18n("commands.list.message")
                .replace("{tps}", String.valueOf(ListUtil.getTps()))
                .replace("{memory}", String.valueOf(ListUtil.getUsedMemory()))
                .replace("{max_memory}", String.valueOf(ListUtil.getTotalMemory()))
                .replace("{player_count}", String.valueOf(playerList.size()))
                .replace("{max_player_count}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{player_list}", playerList.toString()));
    }
}