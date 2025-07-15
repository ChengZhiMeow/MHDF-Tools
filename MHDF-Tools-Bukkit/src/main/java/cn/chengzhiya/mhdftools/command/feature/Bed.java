package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class Bed extends AbstractCommand {
    public Bed() {
        super(
                List.of("bedSettings.enable"),
                "回到床的位置",
                "mhdftools.commands.bed",
                true,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("bedSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                    .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.bed.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (Main.instance.getConfigManager().getConfigManager().getData().getStringList("bedSettings.blackWorld").contains(sender.getWorld().getName())) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("blackWorld"));
            return;
        }

        Location location = sender.getRespawnLocation();
        if (location == null) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.bed.noSleep"));
            return;
        }

        sender.teleport(location);
        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.bed.message"));
    }
}
