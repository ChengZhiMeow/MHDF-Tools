package cn.chengzhimeow.mhdftools.bukkit.module.bed.command;

import cn.chengzhimeow.mhdftools.bukkit.module.bed.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.bed.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.bed.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class Bed extends Command {
    public Bed() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "回到床的位置",
                "mhdftools.commands.bed",
                true,
                ConfigSetting.getInstance().getConfig().commands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 世界黑名单
        if (ConfigSetting.getInstance().getConfig().blackWorld().contains(sender.getWorld().getName())) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().blackWorld());
            return;
        }

        // 输出帮助信息
        if (args.length != 0) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().bed().usage())
                    .replace("{command}", label));
            return;
        }

        Location location = sender.getRespawnLocation();
        if (location == null) {
            sender.sendMessage(LangSetting.getInstance().getConfig().commands().bed().noBed());
            return;
        }

        sender.teleport(location);
        sender.sendMessage(LangSetting.getInstance().getConfig().commands().bed().message());
    }
}
