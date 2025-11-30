package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.ListUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

final class List extends Command {
    public List() {
        super(
                null,
                java.util.List.of("listSettings.enable"),
                "查看在线列表",
                "mhdftools.commands.list",
                false,
                ConfigSetting.getSettingInstance().getData().getStringList("listSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.list.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        java.util.List<String> playerList = ConfigSetting.getSettingInstance().getData().getBoolean("useBungeeCordList")
                                            ? Main.instance.getBungeeCordManager().getPlayerList() : Main.instance.getBungeeCordManager().getBukkitPlayerList();

        ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.list.message")
                .replace("{tps}", String.valueOf(ListUtil.getTps()))
                .replace("{memory}", String.valueOf(ListUtil.getUsedMemory()))
                .replace("{maxMemory}", String.valueOf(ListUtil.getTotalMemory()))
                .replace("{playerCount}", String.valueOf(playerList.size()))
                .replace("{maxPlayerCount}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{playerList}", playerList.toString())
        );
    }
}
