package cn.chengzhimeow.mhdftools.bukkit.hook;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.listener.AbstractPacketListener;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public final class PacketEventsHook extends Hook {
    private ServerVersion serverVersion;

    /**
     * 初始化PacketEvents的API
     */
    @Override
    public void hook() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(Main.instance));
        PacketEvents.getAPI().getSettings()
                .bStats(false)
                .fullStackTrace(true)
                .kickOnPacketException(true)
                .checkForUpdates(false)
                .reEncodeByDefault(false)
                .debug(false);
        PacketEvents.getAPI().load();
        this.serverVersion = PacketEvents.getAPI().getServerManager().getVersion();

        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Object channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
                User user = new User(
                        channel,
                        ConnectionState.PLAY,
                        this.getServerVersion().toClientVersion(),
                        new UserProfile(player.getUniqueId(), player.getName())
                );
                PacketEvents.getAPI().getProtocolManager().setUser(channel, user);
            }
        }

        PacketEvents.getAPI().init();
        super.enable = true;
    }

    /**
     * 卸载PacketEvents的API
     */
    @Override
    public void unhook() {
        if (!super.enable) return;
        super.enable = false;
        PacketEvents.getAPI().terminate();
    }

    /**
     * 给指定用户实例发送指定数据包
     *
     * @param user   接收数据包的用户实例
     * @param packet 发送的数据包
     */
    public void sendPacket(User user, PacketWrapper<?> packet) {
        if (super.isEnable()) user.sendPacket(packet);
    }

    /**
     * 给指定玩家实例发送指定数据包
     *
     * @param player 接收数据包的玩家实例
     * @param packet 发送的数据包
     */
    public void sendPacket(Player player, PacketWrapper<?> packet) {
        if (super.isEnable()) PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    /**
     * 获取指定玩家实例的数据包用户实例
     *
     * @param player 玩家实例
     * @return 数据包用户实例
     */
    public User getUser(Player player) {
        return PacketEvents.getAPI().getPlayerManager().getUser(player);
    }

    /**
     * 注册监听器
     *
     * @param packetListener 数据包监听器实例
     * @param priority       监听器权重
     */
    public void registerListener(AbstractPacketListener packetListener, PacketListenerPriority priority) {
        PacketEvents.getAPI().getEventManager().registerListener(packetListener, priority);
    }
}