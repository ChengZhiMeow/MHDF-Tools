package cn.chengzhimeow.mhdftools.bukkit.module.core.listener;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.core.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.thread.ThreadPool;
import net.nyana.cache.service.CacheService;
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
    private final ThreadPool thread = new ThreadPool(1, "MHDF-Tools ServerTeleport Thread");
    private final CacheService<String, byte[]> cache = MHDFToolsBukkit.getInstance().getCacheManager().createCache("server_teleport", byte[].class);

    public ServerTeleport() {
        super(ModuleMain.instance);
    }

    public void close() {
        this.thread.kill();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        String name = event.getPlayer().getName();
        this.thread.execute(() -> {
            byte[] data = this.cache.get(name);
            if (data == null) return;

            this.cache.remove(name);
            byte[] teleportData = data;
            Bukkit.getScheduler().runTask(MHDFToolsBukkit.getInstance(), () -> {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null || !player.isOnline()) return;

                try {
                    CompoundTag root = NBT.fromBytes(teleportData);

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
                        this.teleport(player, target.getLocation(), 1);
                        return;
                    }

                    if (!BungeeCordManager.getInstance().isBungeeCordMode()) {
                        player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
                        return;
                    }

                    if (mode != 1) {
                        player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
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

                    World world = Bukkit.getWorld(worldName);
                    if (world == null) {
                        player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
                        return;
                    }

                    Location location = new Location(
                            world,
                            info.getDouble("x"),
                            info.getDouble("y"),
                            info.getDouble("z"),
                            info.getFloat("yaw", 0f),
                            info.getFloat("pitch", 0f)
                    );
                    this.teleport(player, location, 1);
                } catch (Exception ignored) {
                    player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
                }
            });
        });
    }

    private void teleport(Player player, Location location, int times) {
        if (!player.isOnline()) return;

        player.teleportAsync(location).thenAccept(success -> {
            if (success) return;
            if (times >= 5) {
                Bukkit.getScheduler().runTask(MHDFToolsBukkit.getInstance(), () ->
                        player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed()));
                return;
            }

            this.thread.schedule(() -> Bukkit.getScheduler().runTask(MHDFToolsBukkit.getInstance(), () ->
                    this.teleport(player, location, times + 1)), 100L);
        });
    }
}
