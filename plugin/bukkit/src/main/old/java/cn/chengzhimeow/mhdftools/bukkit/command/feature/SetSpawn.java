package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class SetSpawn extends Command {
    public SetSpawn() {
        super(
                null,
                List.of("spawnSettings.enable"),
                "设置出生点",
                "mhdftools.commands.setspawn",
                true,
                ConfigSetting.getInstance().getData().getStringList("spawnSettings.setspawnCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            sender.sendMessage(LangSetting.getInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getInstance().i18n("commands.setspawn.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        ConfigurationSection config = ConfigSetting.getInstance().getData().getConfigurationSection("spawnSettings.location");
        if (config == null) {
            return;
        }

        Location location = sender.getLocation();

        config.set("server", Main.instance.getBungeeCordManager().getServerName());
        config.set("world", location.getWorld().getName());
        config.set("x", location.getX());
        config.set("y", location.getY());
        config.set("z", location.getZ());
        config.set("yaw", location.getYaw());
        config.set("pitch", location.getPitch());

        ConfigSetting.getInstance().getData().set("spawnSettings.location", config);
        ConfigSetting.getInstance().save();
        ConfigSetting.getInstance().reload();

        sender.sendMessage(LangSetting.getInstance().i18n("commands.setspawn.message"));
    }
}
