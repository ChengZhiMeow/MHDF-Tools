package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.Command;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class SetSpawn extends Command {
    public SetSpawn() {
        super(
                List.of("spawnSettings.enable"),
                "设置出生点",
                "mhdftools.commands.setspawn",
                true,
                Main.instance.getConfigManager().getConfigManager().getData().getStringList("spawnSettings.setspawnCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("usageError")
                    .replace("{usage}", Main.instance.getConfigManager().getLangManager().i18n("commands.setspawn.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        ConfigurationSection config = Main.instance.getConfigManager().getConfigManager().getData().getConfigurationSection("spawnSettings.location");
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

        Main.instance.getConfigManager().getConfigManager().getData().set("spawnSettings.location", config);
        Main.instance.getConfigManager().getConfigManager().save();
        Main.instance.getConfigManager().getConfigManager().reload();

        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.setspawn.message"));
    }
}
