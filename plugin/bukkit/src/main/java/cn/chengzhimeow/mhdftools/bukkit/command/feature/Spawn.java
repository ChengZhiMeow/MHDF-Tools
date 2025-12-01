package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.SpawnUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class Spawn extends Command {
    public Spawn() {
        super(
                null,
                List.of("spawnSettings.enable"),
                "返回出生点",
                "mhdftools.commands.spawn",
                true,
                ConfigSetting.getInstance().getData().getStringList("spawnSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.spawn.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (ConfigSetting.getInstance().getData().getStringList("spawnSettings.blackWorld").contains(sender.getWorld().getName())) {
            sender.sendMessage(LangSetting.getInstance().i18n("blackWorld"));
            return;
        }

        SpawnUtil.teleportSpawn(sender);
        Main.instance.getBungeeCordManager().sendMessage(sender, LangSetting.getInstance().i18n("commands.spawn.message"));
    }
}
