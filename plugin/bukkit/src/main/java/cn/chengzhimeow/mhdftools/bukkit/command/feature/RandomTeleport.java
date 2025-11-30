package cn.chengzhimeow.mhdftools.bukkit.command.feature;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Command;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.reflection.world.BiomeUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.RandomTeleportUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

final class RandomTeleport extends Command {
    public RandomTeleport() {
        super(
                null,
                List.of("randomTeleportSettings.enable"),
                "随机传送",
                "mhdftools.commands.randomteleport",
                false,
                ConfigSetting.getSettingInstance().getData().getStringList("randomTeleportSettings.commands").toArray(new String[0])
        );
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        Player player = sender instanceof Player ? (Player) sender : null;
        String worldName = player != null ? player.getWorld().getName() : null;
        Biome biome = null;
        String server = null;

        changeRandomTeleportArgs:
        if (args.length >= 1) {
            switch (args[0]) {
                case "world" -> {
                    if (args.length < 2) {
                        ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                                .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.randomteleport.subCommands.world.usage"))
                                .replace("{command}", label)
                        );
                        return;
                    }
                    worldName = args[1];

                    if (args.length >= 3) {
                        if (Bukkit.getPlayer(args[2]) == null) {
                            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("playerOffline"));
                            return;
                        }
                        if (!sender.hasPermission("mhdftools.commands.randomteleport.other")) {
                            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("noPermission"));
                            return;
                        }
                        player = Bukkit.getPlayer(args[2]);
                    }

                    if (args.length >= 4 && !args[3].equals(Main.instance.getBungeeCordManager().getServerName())) {
                        server = args[3];
                    }

                    break changeRandomTeleportArgs;
                }
                case "biome" -> {
                    if (args.length < 3) {
                        ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                                .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.randomteleport.subCommands.biome.usage"))
                                .replace("{command}", label)
                        );
                        return;
                    }

                    worldName = args[1];
                    biome = BiomeUtil.getBiome(args[2]);
                    if (biome == null) {
                        ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("mhdftools.commands.randomteleport.noBiome")
                                .replace("{biome}", args[2])
                        );
                        return;
                    }

                    if (args.length >= 4) {
                        if (Bukkit.getPlayer(args[3]) == null) {
                            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("playerOffline"));
                            return;
                        }
                        if (!sender.hasPermission("mhdftools.commands.randomteleport.other")) {
                            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("noPermission"));
                            return;
                        }
                        player = Bukkit.getPlayer(args[3]);
                    }

                    if (args.length >= 5 && !args[4].equals(Main.instance.getBungeeCordManager().getServerName())) {
                        server = args[4];
                    }

                    break changeRandomTeleportArgs;
                }
            }

            // 输出帮助信息
            {
                ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("commands.randomteleport.subCommands.help.message")
                        .replace("{helpList}", LangSetting.getSettingInstance().getHelpList("commands.randomteleport.subCommands"))
                        .replace("{command}", label)
                );
            }
            return;
        }

        // 输出帮助信息
        if (player == null) {
            ActionUtil.sendMessage(sender, LangSetting.getSettingInstance().i18n("usageError")
                    .replace("{usage}", LangSetting.getSettingInstance().i18n("commands.randomteleport.usage"))
                    .replace("{command}", label)
            );
            return;
        }

        if (server != null) {
            Main.instance.getCacheManager().put("randomTeleportWorld", player.getName(), worldName);
            if (biome != null) {
                Main.instance.getCacheManager().put("randomTeleportBiome", player.getName(), biome.key().value());
            }
            Main.instance.getBungeeCordManager().connectServer(player, server);
            return;
        }

        Player finalPlayer = player;
        String finalWorldName = worldName;
        Biome finalBiome = biome;
        CCScheduler.getInstance().getRegionScheduler().runTask(Main.instance, player.getLocation(), () ->
                RandomTeleportUtil.handleRandomTeleport(sender, finalPlayer, finalWorldName, finalBiome)
        );
    }

    @Override
    public List<String> tabCompleter(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Bukkit.getWorlds().stream()
                    .map(World::getName)
                    .toList();
        }
        if (args.length == 2) {
            return Main.instance.getBungeeCordManager().getPlayerList();
        }
        return new ArrayList<>();
    }
}