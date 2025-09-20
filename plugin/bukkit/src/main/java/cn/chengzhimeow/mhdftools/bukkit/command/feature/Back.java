package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.BackData;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.command.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.menu.feature.BackMenu;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.BackUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Back extends Command {
    public Back() {
        super(
                List.of("backSettings.enable"),
                "返回位置记录",
                "mhdftools.commands.back",
                true,
                ConfigSetting.getSettingInstance().getData().getStringList("backSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        MHDFToolsPlayer player = MHDFToolsAPIHelper.getInstance().getPlayerManager().getPlayer(sender);
        List<BackData> backDataList = new ArrayList<>();
        if (args.length >= 1) {
            switch (args[0]) {
                case "menu" -> {
                    new BackMenu(sender, 1).openMenu();
                    return;
                }
                case "teleport", "death" -> backDataList = player.getBackDataList(args[0], BackUtil.getMaxBack(sender));
            }
        } else {
            backDataList = player.getBackDataList(BackUtil.getMaxBack(sender));
        }

        if (backDataList.isEmpty()) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.back.noLocation"));
            return;
        }

        Main.instance.getBungeeCordManager().teleportLocation(sender, backDataList.get(0).toBungeeCordLocation());
        ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.back.message"));
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("menu", "teleport", "death");
        }
        return new ArrayList<>();
    }
}
