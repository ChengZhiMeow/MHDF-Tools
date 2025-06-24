package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class SetSpawn extends AbstractCommand {
    public SetSpawn() {
        super(
                List.of("spawnSettings.enable"),
                "设置出生点",
                "mhdftools.commands.setspawn",
                true,
                ConfigUtil.getConfig().getStringList("spawnSettings.setspawnCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.setspawn.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("spawnSettings.location");
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

        ConfigUtil.getConfig().set("spawnSettings.location", config);
        ConfigUtil.saveConfig();
        ConfigUtil.reloadConfig();

        ActionUtil.sendMessage(sender, LangUtil.i18n("commands.setspawn.message"));
    }
}
