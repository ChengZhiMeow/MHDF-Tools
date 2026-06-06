package cn.chengzhimeow.mhdftools.bukkit.module.bungee.manager;

import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.bungee.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.bungee.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.bungee.listener.PluginMessage;
import cn.chengzhimeow.mhdftools.console.LogManager;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public final class BungeeCordManagerImpl extends BungeeCordManager {
    @Getter(lazy = true)
    private static final BungeeCordManagerImpl instance = new BungeeCordManagerImpl();
    private static final String POST_FAILED = "发送插件消息失败 | 原因: {}";

    @Getter
    private final PluginMessage listener = new PluginMessage();
    @Getter
    @Setter
    private String serverName = "无";
    @Setter
    private Set<String> playerList = new HashSet<>();

    private BungeeCordManagerImpl() {
    }

    @Override
    public boolean isBungeeCordMode() {
        return ConfigSetting.getInstance().getConfig().enable();
    }

    @Override
    public List<String> getBukkitPlayerList() {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .toList();
    }

    @Override
    public List<String> getPlayerList() {
        if (this.isBungeeCordMode()) return new ArrayList<>(this.playerList);
        return new ArrayList<>(this.getBukkitPlayerList());
    }

    @Override
    public boolean ifPlayerOnline(String name) {
        if (Bukkit.getPlayer(name) != null) return true;
        return this.playerList.contains(name);
    }

    /**
     * 给BC插件通道发送指定消息数据实例
     *
     * @param out 消息数据实例
     */
    private void post(ByteArrayDataOutput out) {
        if (!this.isBungeeCordMode()) {
            LogManager.instance.debug(BungeeCordManagerImpl.POST_FAILED, "未开启群组模式");
            return;
        }

        Iterator<? extends Player> iterator = Bukkit.getOnlinePlayers().iterator();
        if (!iterator.hasNext()) {
            LogManager.instance.debug(BungeeCordManagerImpl.POST_FAILED, "服务器没有玩家");
            return;
        }

        iterator.next().sendPluginMessage(ModuleMain.instance.getPlugin(), "BungeeCord", out.toByteArray());
    }

    /**
     * 更新BC玩家列表数据
     */
    private void updatePlayerList() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF("ALL");

        this.post(out);
    }

    /**
     * 更新BC服务器名称数据
     */
    private void updateServerName() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer");

        this.post(out);
    }

    /**
     * 更新数据
     */
    public void updateData() {
        this.updatePlayerList();
        this.updateServerName();
    }
}
