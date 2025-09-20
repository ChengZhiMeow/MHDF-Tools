package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.command.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import org.bukkit.Location;
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
                ConfigSetting.getSettingInstance().getData().getStringList("spawnSettings.setspawnCommands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        // 输出帮助信息
        if (args.length != 0) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.setspawn.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        ConfigurationSection config = ConfigSetting.getSettingInstance().getData().getConfigurationSection("spawnSettings.location");
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

        ConfigSetting.getSettingInstance().getData().set("spawnSettings.location", config);
        ConfigSetting.getSettingInstance().save();
        ConfigSetting.getSettingInstance().reload();

        ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.setspawn.message"));
    }
}
