package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.BackData;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.menu.feature.BackMenu;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.feature.BackUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Back extends AbstractCommand {
    public Back() {
        super(
                List.of("backSettings.enable"),
                "返回位置记录",
                "mhdftools.commands.back",
                true,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("backSettings.commands").toArray(new String[0])
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
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.back.noLocation"));
            return;
        }

        Main.instance.getBungeeCordManager().teleportLocation(sender, backDataList.get(0).toBungeeCordLocation());
        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.back.message"));
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("menu", "teleport", "death");
        }
        return new ArrayList<>();
    }
}
