package cn.chengzhimeow.mhdftools.bukkit.module.spawn.command;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.spawn.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.spawn.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.spawn.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.spawn.config.SpawnSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class Spawn extends Command {
    public Spawn() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "返回出生点",
                "mhdftools.commands.spawn",
                true,
                ConfigSetting.getInstance().getConfig().spawnCommands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 0) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().spawn().usage())
                    .replace("{command}", label));
            return;
        }

        if (ConfigSetting.getInstance().getConfig().blackWorld().contains(sender.getWorld().getName())) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().blackWorld());
            return;
        }

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(sender.getUniqueId(), sender.getName());
        mhdfPlayer.teleport(SpawnSetting.getInstance().getConfig().location());
        mhdfPlayer.sendMessage(LangSetting.getInstance().getConfig().commands().spawn().message());
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
