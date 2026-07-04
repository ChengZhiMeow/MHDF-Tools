package cn.chengzhimeow.mhdftools.bukkit.module.core.listener;

import cn.chengzhimeow.mhdftools.api.entity.location.BukkitLocation;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkitAdapt;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.core.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.thread.ThreadPool;
import net.nyana.nbt.NBT;
import net.nyana.nbt.tag.CompoundTag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public final class ServerTeleport extends Listener {
    private static final ThreadPool thread = new ThreadPool(1, "MHDF-Tools ServerTeleport Thread");

    public static void close() {
        thread.kill();
    }

    public ServerTeleport() {
        super(ModuleMain.instance);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        String name = event.getPlayer().getName();

        thread.execute(() -> {
            byte[] data = ModuleMain.instance.serverTeleportCache.get(name);
            if (data == null) return;

            ModuleMain.instance.serverTeleportCache.remove(name);
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) return;

            try {
                CompoundTag root = NBT.fromBytes(data);
                if (root == null) {
                    player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
                    return;
                }

                CompoundTag info = root.getCompound("info", root);
                if (info == null) {
                    player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
                    return;
                }

                byte mode = root.getByte("mode");

                if (mode == 0) {
                    String targetName = info.getString("player");
                    if (targetName == null) {
                        player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
                        return;
                    }

                    Player target = Bukkit.getPlayerExact(targetName);
                    if (target == null || !target.isOnline()) {
                        player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
                        return;
                    }

                    teleport(player, MHDFToolsBukkitAdapt.adapt(target.getLocation()));
                    return;
                }

                String targetServer = info.getString("server_id");
                if (targetServer == null) {
                    player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
                    return;
                }

                if (!targetServer.equalsIgnoreCase(BungeeCordManager.getInstance().getServerName())) {
                    player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
                    return;
                }

                String worldName = info.getString("world");
                if (worldName == null) {
                    player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
                    return;
                }

                BukkitLocation location = new BukkitLocation(
                        worldName,
                        info.getDouble("x"),
                        info.getDouble("y"),
                        info.getDouble("z"),
                        info.getFloat("yaw", 0f),
                        info.getFloat("pitch", 0f)
                );
                teleport(player, location);
            } catch (Exception ignored) {
                player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
            }
        });
    }

    public static void teleport(Player player, BukkitLocation location) {
        teleport(player, location, 1);
    }

    private static void teleport(Player player, BukkitLocation location, int times) {
        if (!player.isOnline()) return;

        World world = Bukkit.getWorld(location.getWorld());
        if (world == null) {
            retry(player, location, times);
            return;
        }

        player.teleportAsync(new Location(
                world,
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        )).thenAccept(success -> {
            if (success) return;
            if (times >= 5) {
                player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
                return;
            }

            thread.schedule(() -> teleport(player, location, times + 1), 100L);
        });
    }

    private static void retry(Player player, BukkitLocation location, int times) {
        if (times >= 5) {
            player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
            return;
        }

        thread.schedule(() -> teleport(player, location, times + 1), 100L);
    }
}
