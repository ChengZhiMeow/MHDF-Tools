package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class Bed extends Command {
    public Bed() {
        super(
                null,
                List.of("bedSettings.enable"),
                "回到床的位置",
                "mhdftools.commands.bed",
                true,
                ConfigSetting.getInstance().getData().getStringList("bedSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.bed.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (ConfigSetting.getInstance().getData().getStringList("bedSettings.blackWorld").contains(sender.getWorld().getName())) {
            sender.sendMessage(LangSetting.getInstance().i18n("blackWorld"));
            return;
        }

        Location location = sender.getRespawnLocation();
        if (location == null) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.bed.noSleep"));
            return;
        }

        sender.teleport(location);
        sender.sendMessage(LangSetting.getInstance().i18n("commands.bed.message"));
    }
}
