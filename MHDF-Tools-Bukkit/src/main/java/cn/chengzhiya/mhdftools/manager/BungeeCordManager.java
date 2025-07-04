package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhiya.mhdftools.listener.misc.PluginMessage;
import cn.chengzhiya.mhdftools.manager.cache.MHDFCacheManager;
import cn.chengzhiya.mhdftools.manager.cache.impl.RedisCacheManager;
import cn.chengzhiya.mhdftools.text.TextComponent;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.feature.AtUtil;
import cn.chengzhiya.mhdftools.util.message.LogUtil;
import cn.chengzhiya.mhdftools.util.teleport.TeleportUtil;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.bukkit.Bukkit.getServer;

@Getter
public final class BungeeCordManager {
    private final PluginMessageListener messageListener = new PluginMessage();
    @Setter
    private List<String> bungeeCordPlayerList = new CopyOnWriteArrayList<>();
    @Setter
    private String serverName = "无";

    /**
     * 初始化群组模式
     */
    public void init() {
        if (isBungeeCordMode()) {
            getServer().getMessenger().registerOutgoingPluginChannel(Main.instance, "BungeeCord");
            getServer().getMessenger().registerIncomingPluginChannel(Main.instance, "BungeeCord", getMessageListener());
        }
    }

    /**
     * 关闭群组模式
     */
    public void close() {
        if (isBungeeCordMode()) {
            getServer().getMessenger().unregisterOutgoingPluginChannel(Main.instance, "BungeeCord");
            getServer().getMessenger().unregisterIncomingPluginChannel(Main.instance, "BungeeCord", getMessageListener());
        }
    }

    /**
     * 检测是否开启群组模式
     *
     * @return 结果
     */
    public boolean isBungeeCordMode() {
        return ConfigUtil.getConfig().getBoolean("bungeeCordSettings.enable");
    }

    /**
     * 给BC插件通道发送指定消息数据实例
     *
     * @param out 消息数据实例
     */
    private void sendPluginMessage(ByteArrayDataOutput out) {
        if (!isBungeeCordMode()) {
            LogUtil.debug("发送插件消息失败 | 原因: {}",
                    "未开启群组模式"
            );
            return;
        }

        List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (playerList.isEmpty()) {
            LogUtil.debug("发送插件消息失败 | 原因: {}",
                    "服务器没有玩家"
            );
            return;
        }

        playerList.get(0).sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
    }

    /**
     * 发送梦之工具插件消息
     *
     * @param data 消息数据实例
     */
    private void sendMhdfToolsPluginMessage(JSONObject data) {
        if (data.getJSONObject("params") == null) {
            data.put("params", new JSONObject());
        }

        LogUtil.debug("发送梦之工具插件消息至群组端 | 消息: {}",
                data.toJSONString()
        );

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("mhdf_tools");
        out.writeUTF(data.toJSONString());

        sendPluginMessage(out);
    }

    /**
     * 将指定玩家ID的玩家移动到指定服务器ID的服务器
     *
     * @param playerName 玩家ID
     * @param serverName 服务器ID
     */
    public void connectServer(String playerName, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(playerName);
        out.writeUTF(serverName);

        sendPluginMessage(out);
    }

    /**
     * 将指定玩家实例的玩家移动到指定服务器ID的服务器
     *
     * @param player     玩家实例
     * @param serverName 服务器ID
     */
    public void connectServer(OfflinePlayer player, String serverName) {
        connectServer(player.getName(), serverName);
    }

    /**
     * 传送指定玩家ID到指定玩家ID的服务器
     *
     * @param playerName 被传送的玩家ID
     * @param targetName 传送到的玩家ID
     */
    public void teleportPlayer(String playerName, String targetName) {
        Player player = Bukkit.getPlayer(playerName);
        Player target = Bukkit.getPlayer(targetName);
        if (player != null && target != null) {
            TeleportUtil.teleport(player, target.getLocation(), new ConcurrentHashMap<>());
            return;
        }
        Main.instance.getCacheManager().put("tpPlayer", playerName, targetName);

        JSONObject data = new JSONObject();
        data.put("action", "teleportPlayer");

        JSONObject params = new JSONObject();
        params.put("playerName", playerName);
        params.put("targetName", targetName);

        data.put("params", params);

        sendMhdfToolsPluginMessage(data);
    }

    /**
     * 传送指定玩家实例到指定玩家ID的服务器
     *
     * @param player     被传送的玩家实例
     * @param targetName 传送到的玩家ID
     */
    public void teleportPlayer(OfflinePlayer player, String targetName) {
        teleportPlayer(player.getName(), targetName);
    }

    /**
     * 传送指定玩家实例到指定玩家ID的服务器
     *
     * @param playerName 被传送的玩家ID
     * @param target     传送到的玩家实例
     */
    public void teleportPlayer(String playerName, OfflinePlayer target) {
        teleportPlayer(playerName, target.getName());
    }

    /**
     * 传送指定玩家实例到指定玩家实例的服务器
     *
     * @param player 被传送的玩家实例
     * @param target 传送到的玩家实例
     */
    public void teleportPlayer(OfflinePlayer player, OfflinePlayer target) {
        teleportPlayer(player, target.getName());
    }

    /**
     * 将指定玩家ID传送至指定群组位置实例
     *
     * @param playerName         玩家ID
     * @param bungeeCordLocation 群组位置实例
     */
    public void teleportLocation(String playerName, BungeeCordLocation bungeeCordLocation) {
        if (!isBungeeCordMode() || bungeeCordLocation.getServer().equals(getServerName())) {
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                return;
            }

            player.teleportAsync(bungeeCordLocation.toLocation());
            return;
        }
        Main.instance.getCacheManager().put("tpLocation", playerName, bungeeCordLocation.toString());

        connectServer(playerName, bungeeCordLocation.getServer());
    }

    /**
     * 将指定玩家实例传送至指定群组位置实例
     *
     * @param player             玩家实例
     * @param bungeeCordLocation 群组位置实例
     */
    public void teleportLocation(Player player, BungeeCordLocation bungeeCordLocation) {
        teleportLocation(player.getName(), bungeeCordLocation);
    }

    /**
     * 向指定玩家ID发送指定消息文本
     *
     * @param playerName 玩家ID
     * @param message    消息文本
     */
    public void sendMessage(String playerName, String message) {
        if (!isBungeeCordMode() && playerName.equals("all")) {
            ActionUtil.broadcastMessage(message);
            return;
        }

        if (!isBungeeCordMode() && playerName.equals("console")) {
            LogUtil.log(message);
            return;
        }

        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            ActionUtil.sendMessage(player, message);
            return;
        }

        JSONObject data = new JSONObject();
        data.put("playerName", playerName);
        data.put("message", message);

        ((RedisCacheManager) ((MHDFCacheManager) Main.instance.getCacheManager()).getCacheManager())
                .getRedisClient()
                .getRedisMessageManager()
                .sendRedisMessage("sendMessage", data.toJSONString());
    }

    /**
     * 向指定玩家ID发送指定消息文本
     *
     * @param playerName 玩家ID
     * @param message    文本实例
     */
    public void sendMessage(String playerName, TextComponent message) {
        sendMessage(playerName, message.toMiniMessageString());
    }

    /**
     * 向指定玩家实例发送指定消息文本
     *
     * @param player  玩家实例
     * @param message 消息文本
     */
    public void sendMessage(OfflinePlayer player, String message) {
        sendMessage(player.getName(), message);
    }

    /**
     * 向指定玩家ID发送指定消息文本
     *
     * @param player  玩家实例
     * @param message 文本实例
     */
    public void sendMessage(OfflinePlayer player, TextComponent message) {
        sendMessage(player, message.toMiniMessageString());
    }

    /**
     * 向指定玩家ID发送指定消息文本
     *
     * @param message 消息文本
     */
    public void broadcastMessage(String message) {
        sendMessage("all", message);
    }

    /**
     * 修改指定玩家ID的游戏模式为游戏模式实例
     *
     * @param playerName 玩家ID
     * @param gameMode   游戏模式实例
     */
    public void setGameMode(String playerName, GameMode gameMode) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            player.setGameMode(gameMode);
            return;
        }

        JSONObject data = new JSONObject();
        data.put("playerName", playerName);
        data.put("gameMode", gameMode.name());

        ((RedisCacheManager) Main.instance.getCacheManager()).getRedisClient().getRedisMessageManager()
                .sendRedisMessage("setGameMode", data.toJSONString());
    }

    /**
     * 修改指定玩家实例的游戏模式为游戏模式实例
     *
     * @param player   玩家实例
     * @param gameMode 游戏模式实例
     */
    public void setGameMode(OfflinePlayer player, GameMode gameMode) {
        setGameMode(player.getName(), gameMode);
    }

    /**
     * at玩家列表
     *
     * @param atList 玩家列表
     */
    public void atList(Set<String> atList, String by) {
        if (!isBungeeCordMode()) {
            AtUtil.atList(atList, by);
            return;
        }

        JSONObject data = new JSONObject();
        data.put("atList", atList);
        data.put("by", by);

        ((RedisCacheManager) ((MHDFCacheManager) Main.instance.getCacheManager()).getCacheManager())
                .getRedisClient()
                .getRedisMessageManager()
                .sendRedisMessage("atList", data.toJSONString());
    }

    /**
     * 更新BC玩家列表数据
     */
    public void updateBungeeCordPlayerList() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF("ALL");

        sendPluginMessage(out);
    }

    /**
     * 获取在线玩家列表
     *
     * @return 在线玩家列表
     */
    public List<String> getPlayerList() {
        if (isBungeeCordMode()) {
            return new ArrayList<>(getBungeeCordPlayerList());
        }

        return new ArrayList<>(getBukkitPlayerList());
    }

    /**
     * 获取子服在线玩家列表
     *
     * @return 子服在线玩家列表
     */
    public List<String> getBukkitPlayerList() {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .toList();
    }

    /**
     * 更新BC服务器名称数据
     */
    public void updateServerName() {
        JSONObject data = new JSONObject();
        data.put("action", "serverInfo");
        data.put("to", "me");

        sendMhdfToolsPluginMessage(data);
    }

    /**
     * 判断指定玩家ID的玩家是否在线
     *
     * @param name 玩家ID
     * @return 结果
     */
    public boolean ifPlayerOnline(String name) {
        return getPlayerList().contains(name);
    }
}
