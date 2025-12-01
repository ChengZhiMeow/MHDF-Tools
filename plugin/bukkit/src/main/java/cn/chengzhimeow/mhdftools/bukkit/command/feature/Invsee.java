package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.InvseeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class Invsee extends Command {
    public Invsee() {
        super(
                null,
                List.of("invseeSettings.enable"),
                "查看背包",
                "mhdftools.commands.invsee",
                true,
                ConfigSetting.getInstance().getData().getStringList("invseeSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 2) {
            sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.invsee.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(LangSetting.getInstance().i18n("playerOffline"));
            return;
        }

        if (InvseeUtil.invsee(sender, target, args[1])) {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.invsee.message")
                    .replace("{type}", LangSetting.getInstance().i18n("commands.invsee.types." + args[1]))
                    .replace("{player}", target.getName())
            );
        } else {
            sender.sendMessage(LangSetting.getInstance().i18n("commands.invsee.noType")
                    .replace("{type}", args[1])
            );
        }
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        if (args.length == 2) {
            return Arrays.asList("inventory", "enderchest", "armor");
        }
        return new ArrayList<>();
    }
}
