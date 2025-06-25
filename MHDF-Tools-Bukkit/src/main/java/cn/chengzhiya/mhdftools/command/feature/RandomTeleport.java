package cn.chengzhiya.mhdftools.command.feature;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.feature.RandomTeleportUtil;
import cn.chengzhiya.mhdftools.util.world.BiomeUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class RandomTeleport extends AbstractCommand {
    public RandomTeleport() {
        super(
                List.of("randomTeleportSettings.enable"),
                "随机传送",
                "mhdftools.commands.randomteleport",
                false,
                ConfigUtil.getConfig().getStringList("randomTeleportSettings.commands").toArray(new String[0])
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
                        ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                                .replace("{usage}", LangUtil.i18n("commands.randomteleport.subCommands.world.usage"))
                                .replace("{command}", label)
                        );
                        return;
                    }
                    worldName = args[1];

                    if (args.length >= 3) {
                        if (Bukkit.getPlayer(args[2]) == null) {
                            ActionUtil.sendMessage(sender, LangUtil.i18n("playerOffline"));
                            return;
                        }
                        if (!sender.hasPermission("mhdftools.commands.randomteleport.other")) {
                            ActionUtil.sendMessage(sender, LangUtil.i18n("noPermission"));
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
                        ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                                .replace("{usage}", LangUtil.i18n("commands.randomteleport.subCommands.biome.usage"))
                                .replace("{command}", label)
                        );
                        return;
                    }

                    worldName = args[1];
                    biome = BiomeUtil.getBiome(args[2]);
                    if (biome == null) {
                        ActionUtil.sendMessage(sender, LangUtil.i18n("mhdftools.commands.randomteleport.noBiome"));
                        return;
                    }

                    if (args.length >= 4) {
                        if (Bukkit.getPlayer(args[3]) == null) {
                            ActionUtil.sendMessage(sender, LangUtil.i18n("playerOffline"));
                            return;
                        }
                        if (!sender.hasPermission("mhdftools.commands.randomteleport.other")) {
                            ActionUtil.sendMessage(sender, LangUtil.i18n("noPermission"));
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
                ActionUtil.sendMessage(sender, LangUtil.i18n("commands.randomteleport.subCommands.help.message")
                        .replace("{helpList}", LangUtil.getHelpList("commands.randomteleport.subCommands"))
                        .replace("{command}", label)
                );
            }
            return;
        }

        // 输出帮助信息
        if (player == null) {
            ActionUtil.sendMessage(sender, LangUtil.i18n("usageError")
                    .replace("{usage}", LangUtil.i18n("commands.randomteleport.usage"))
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

        final World world = Bukkit.getWorld(worldName);
        if (world == null) return;

        final Player finalPlayer = player;
        final Biome finalBiome = biome;

        MHDFScheduler.getRegionScheduler().runTask(Main.instance, player.getLocation(), () ->
                RandomTeleportUtil.handleRandomTeleport(sender, finalPlayer, world, finalBiome)
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