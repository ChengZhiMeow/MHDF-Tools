package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.enums.RandomTeleportStatus;
import cn.chengzhiya.mhdftools.util.GroupUtil;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.math.RandomUtil;
import cn.chengzhiya.mhdftools.util.teleport.TeleportUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.BiomeSearchResult;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class RandomTeleportUtil {
    /**
     * 获取指定玩家实例的组配置实例
     *
     * @param player 玩家实例
     * @return 组配置实例
     */
    public static ConfigurationSection getGroupConfigurationSection(Player player) {
        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("randomTeleportSettings");
        if (config == null) {
            return null;
        }

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
     */
    public static RandomTeleportStatus randomTeleport(Player player, World world, Biome biome, int times) {
        if (times <= 0) {
            return RandomTeleportStatus.OUT_TRY_TIMES;
        }
        ConfigurationSection group = getGroupConfigurationSection(player);
        if (group == null) {
            return RandomTeleportStatus.NO_GROUP_CONFIG;
        }

        int min = group.getInt("min");
        int max = group.getInt("max");

        List<String> blackBlock = group.getStringList("blackBlock");
        int centerX = RandomUtil.randomInt(min, max);
        int centerZ = RandomUtil.randomInt(min, max);

        Location centerLocation = new Location(world, centerX, 60, centerZ);

        if (biome != null) {
            BiomeSearchResult result = world.locateNearestBiome(new Location(world, centerX, 60, centerZ), max, 64, 64, biome);
            if (result == null) {
                return RandomTeleportStatus.NO_BIOME;
            }
            centerLocation = result.getLocation();
        }

        for (int y = world.getMaxHeight(); y > 60; y--) {
            Location location = centerLocation.clone();
            location.setY(y);

            Block block = location.getBlock();
            if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR || block.getType() == Material.VOID_AIR) {
                continue;
            }
            if (blackBlock.contains(block.getType().name())) {
                continue;
            }
            location.setY(location.getY() + 1);

            TeleportUtil.teleport(player, location, new ConcurrentHashMap<>());
            return RandomTeleportStatus.SUCCESS;
        }

        return randomTeleport(player, world, biome, times - 1);
    }

    /**
     * 将指定玩家在指定世界实例随机传送
     *
     * @param player 玩家实例
     * @param world  世界实例
     * @param times  剩余尝试次数
     */
    public static RandomTeleportStatus randomTeleport(Player player, World world, int times) {
        return randomTeleport(player, world, null, times);
    }

    /**
     * 将指定玩家在指定世界实例随机传送
     *
     * @param player 玩家实例
     * @param world  世界实例
     */
    public static RandomTeleportStatus randomTeleport(Player player, World world) {
        return randomTeleport(player, world, ConfigUtil.getConfig().getInt("randomTeleportSettings.maxTryTime"));
    }

    /**
     * 将指定玩家在当前世界实例随机传送
     *
     * @param player 玩家实例
     */
    public static RandomTeleportStatus randomTeleport(Player player) {
        return randomTeleport(player, player.getWorld());
    }


    /**
     * 处理随机传送命令
     *
     * @param sender 命令执行者实例
     * @param player 玩家实例
     * @param world  世界实例
     * @param biome  群系实例
     */
    public static void handleRandomTeleport(CommandSender sender, Player player, World world, Biome biome) {
        long startTime = System.currentTimeMillis();
        int maxTryTime = ConfigUtil.getConfig().getInt("randomTeleportSettings.maxTryTime");
        RandomTeleportStatus status = RandomTeleportUtil.randomTeleport(player, world, biome, maxTryTime);
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;
        switch (status) {
            case SUCCESS -> ActionUtil.sendMessage(sender, LangUtil.i18n("commands.randomteleport.message")
                    .replace("{duration}", String.valueOf(duration))
            );
            case NO_BIOME -> ActionUtil.sendMessage(sender, LangUtil.i18n("commands.randomteleport.noBiome"));
            case OUT_TRY_TIMES -> ActionUtil.sendMessage(sender, LangUtil.i18n("commands.randomteleport.outTryTime")
                    .replace("{amount}", String.valueOf(maxTryTime))
            );
        }
    }
}
