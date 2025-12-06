package cn.chengzhimeow.mhdftools.bukkit.module.bed.command;

import cn.chengzhimeow.mhdftools.bukkit.module.bed.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.bed.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.bed.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class Bed extends Command {
    public Bed() {
        super(
                ModuleMain.instance,
                List.of("enable"),
                "回到床的位置",
                "mhdftools.commands.bed",
                true,
                ConfigSetting.getInstance().getData().getStringList("commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 世界黑名单
        if (ConfigSetting.getInstance().getData().getStringList("black_world").contains(sender.getWorld().getName())) {
            sender.sendMessage(GlobalLangSetting.getInstance().i18n("black_world"));
            return;
        }

        // 输出帮助信息
        if (args.length != 0) {
            sender.sendMessage(GlobalLangSetting.getInstance().i18n("usage_error")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.bed.usage"))
                    .replace("{command}", label));
            return;
        }

        Location location = sender.getRespawnLocation();
        if (location == null) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.bed.no_bed"));
            return;
        }

        sender.teleport(location);
        sender.sendMessage(LangSetting.getInstance().i18n("commands.bed.message"));
    }
}