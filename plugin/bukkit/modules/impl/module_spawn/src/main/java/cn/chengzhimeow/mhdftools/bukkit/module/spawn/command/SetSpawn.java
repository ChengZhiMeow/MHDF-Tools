package cn.chengzhimeow.mhdftools.bukkit.module.spawn.command;

import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkitAdapt;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.module.spawn.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.spawn.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.spawn.config.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.spawn.config.SpawnSetting;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

final class SetSpawn extends Command {
    public SetSpawn() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                "设置出生点",
                "mhdftools.commands.setspawn",
                true,
                ConfigSetting.getInstance().getConfig().setspawnCommands().toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 0) {
            sender.sendMessage(GlobalLangSetting.getInstance().getConfig().usageError()
                    .replace("{usage}", LangSetting.getInstance().getConfig().commands().setspawn().usage())
                    .replace("{command}", label));
            return;
        }

        Location location = sender.getLocation();
        SpawnSetting.getInstance().setLocation(new BungeeCordLocation(MHDFToolsBukkitAdapt.adapt(location)));

        sender.sendMessage(LangSetting.getInstance().getConfig().commands().setspawn().message());
    }
}
