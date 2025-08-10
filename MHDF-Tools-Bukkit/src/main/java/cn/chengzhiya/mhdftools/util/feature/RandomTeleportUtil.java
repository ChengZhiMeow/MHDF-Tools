package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.enums.RandomTeleportStatus;
import cn.chengzhiya.mhdftools.util.GroupUtil;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.math.RandomUtil;
import cn.chengzhiya.mhdftools.util.teleport.TeleportUtil;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.BiomeSearchResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class RandomTeleportUtil {

    /**
     * 获取指定玩家实例的组配置实例
     *
     * @param player 玩家实例
     * @return 组配置实例
     */
    public static ConfigurationSection getGroupConfigurationSection(Player player) {
        ConfigurationSection config = Main.instance.getConfigManager().getConfigManager().getData().getConfigurationSection("randomTeleportSettings");
        if (config == null) return null;

        String groupId = GroupUtil.getGroup(player, config, "mhdftools.group.randomteleport.");
        return config.getConfigurationSection(groupId);
    }

    /**
     * 将指定玩家在指定世界实例随机传送
     *
     * @param player 玩家实例
     * @param world  世界实例
     * @param biome  群系实例
     * @param times  剩余尝试次数
     * @return 传送结果
     */
    public static CompletableFuture<RandomTeleportStatus> randomTeleport(Player player, World world, Biome biome, int times) {
        CompletableFuture<RandomTeleportStatus> future = new CompletableFuture<>();

        if (times <= 0) {
            future.complete(RandomTeleportStatus.OUT_TRY_TIMES);
            return future;
        }

        ConfigurationSection group = RandomTeleportUtil.getGroupConfigurationSection(player);
        if (group == null) {
            future.complete(RandomTeleportStatus.NO_GROUP_CONFIG);
            return future;
        }

        int min = group.getInt("min");
        int max = group.getInt("max");
        List<String> blackBlock = group.getStringList("blackBlock");

        int centerX = RandomUtil.randomInt(min, max);
        int centerZ = RandomUtil.randomInt(min, max);
        Location centerLocation = new Location(world, centerX, 60, centerZ);

        if (biome != null) {
            BiomeSearchResult result = world.locateNearestBiome(centerLocation, max, 64, 64, biome);
            if (result == null) {
                future.complete(RandomTeleportStatus.NO_BIOME);
                return future;
            }
            centerLocation = result.getLocation();
        }

        Location finalCenterLocation = centerLocation;
        MHDFScheduler.getRegionScheduler().runTask(Main.instance, finalCenterLocation, () -> {
            int topY = world.getHighestBlockYAt(finalCenterLocation);
            Location checkLocation = finalCenterLocation.clone();
            checkLocation.setY(topY);
            Material type = checkLocation.getBlock().getType();

            if (type == Material.AIR || type == Material.CAVE_AIR || type == Material.VOID_AIR ||
                    blackBlock.contains(type.name())) {
                RandomTeleportUtil.randomTeleport(player, world, biome, times - 1).thenAccept(future::complete);
                return;
            }

            Location safeLocation = checkLocation.clone().add(0, 1, 0);
            TeleportUtil.teleport(player, safeLocation, new ConcurrentHashMap<>());
            future.complete(RandomTeleportStatus.SUCCESS);
        });

        return future;
    }

    /**
     * 无指定群系传送
     */
    public static CompletableFuture<RandomTeleportStatus> randomTeleport(Player player, World world, int times) {
        return RandomTeleportUtil.randomTeleport(player, world, null, times);
    }

    /**
     * 使用配置次数自动尝试
     */
    public static CompletableFuture<RandomTeleportStatus> randomTeleport(Player player, World world) {
        return RandomTeleportUtil.randomTeleport(player, world, Main.instance.getConfigManager().getConfigManager().getData().getInt("randomTeleportSettings.maxTryTime"));
    }

    /**
     * 当前世界传送
     */
    public static CompletableFuture<RandomTeleportStatus> randomTeleport(Player player) {
        return RandomTeleportUtil.randomTeleport(player, player.getWorld());
    }

    /**
     * 处理随机传送
     *
     * @param sender    命令执行者实例
     * @param player    玩家实例
     * @param worldName 世界名称
     * @param biome     群系实例（可为 null）
     */
    @SneakyThrows
    public static void handleRandomTeleport(CommandSender sender, Player player, String worldName, Biome biome) {
        long startTime = System.currentTimeMillis();
        int maxTryTime = Main.instance.getConfigManager().getConfigManager().getData().getInt("randomTeleportSettings.maxTryTime");

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.randomteleport.noWorld")
                    .replace("{biome}", Main.instance.getMinecraftLangManager().getBiomeName(biome))
            );
            return;
        }

        RandomTeleportUtil.randomTeleport(player, world, biome, maxTryTime).thenAccept(status -> {
            long duration = System.currentTimeMillis() - startTime;

            switch (status) {
                case SUCCESS ->
                        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.randomteleport.message")
                                .replace("{duration}", String.valueOf(duration)));
                case NO_BIOME ->
                        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.randomteleport.noBiome")
                                .replace("{biome}", Main.instance.getMinecraftLangManager().getBiomeName(biome))
                        );
                case OUT_TRY_TIMES ->
                        ActionUtil.sendMessage(sender, Main.instance.getConfigManager().getLangManager().i18n("commands.randomteleport.outTryTime")
                                .replace("{amount}", String.valueOf(maxTryTime))
                        );
                case NO_GROUP_CONFIG -> ActionUtil.sendMessage(sender, "§c未找到传送区域配置。");
            }
        });
    }
}
