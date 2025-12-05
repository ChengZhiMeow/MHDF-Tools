package cn.chengzhimeow.mhdftools.bukkit.module.bungee.manager;

import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.bungee.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.bungee.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.bungee.listener.PluginMessage;
import cn.chengzhimeow.mhdftools.console.LogManager;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private List<String> playerList = new ArrayList<>();

    private BungeeCordManagerImpl() {
    }

    @Override
    public boolean isBungeeCordMode() {
        return ConfigSetting.getInstance().getData().getBoolean("enable");
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
     * 发送梦之工具插件消息
     *
     * @param data 消息数据实例
     */
    private void postWithMHDFTools(JSONObject data) {
        if (data.getJSONObject("params") == null) data.put("params", new JSONObject());

        LogManager.instance.debug("发送梦之工具插件消息至群组端 | 消息: {}",
                data.toJSONString()
        );

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("mhdf_tools");
        out.writeUTF(data.toJSONString());
        this.post(out);
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
        JSONObject data = new JSONObject();
        data.put("action", "server_info");
        data.put("to", "me");

        this.postWithMHDFTools(data);
    }

    /**
     * 更新数据
     */
    public void updateData() {
        this.updatePlayerList();
        this.updateServerName();
    }
}
