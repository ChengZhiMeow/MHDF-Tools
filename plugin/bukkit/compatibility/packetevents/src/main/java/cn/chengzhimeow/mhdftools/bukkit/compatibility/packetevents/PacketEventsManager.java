package cn.chengzhimeow.mhdftools.bukkit.compatibility.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class PacketEventsManager {
    @Getter(lazy = true)
    private static final PacketEventsManager instance = new PacketEventsManager();
    private JavaPlugin plugin;
    private ServerVersion serverVersion;

    private PacketEventsManager() {
    }

    /**
     * 初始化PacketEvents的API
     */
    public void hook(JavaPlugin plugin) {
        this.plugin = plugin;
        this.serverVersion = PacketEvents.getAPI().getServerManager().getVersion();
    }

    /**
     * 卸载PacketEvents的API
     */
    public void unhook() {
        PacketEvents.getAPI().terminate();
    }

    /**
     * 给指定用户实例发送指定数据包
     *
     * @param user   接收数据包的用户实例
     * @param packet 发送的数据包
     */
    public void sendPacket(User user, PacketWrapper<?> packet) {
        user.sendPacket(packet);
    }

    /**
     * 给指定玩家实例发送指定数据包
     *
     * @param player 接收数据包的玩家实例
     * @param packet 发送的数据包
     */
    public void sendPacket(Player player, PacketWrapper<?> packet) {
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
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
    public void registerListener(PacketListener packetListener, PacketListenerPriority priority) {
        PacketEvents.getAPI().getEventManager().registerListener(packetListener, priority);
    }
}
