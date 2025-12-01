package cn.chengzhimeow.mhdftools.bukkit.manager;

import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.manager.cache.impl.RedisCacheManager;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import cn.chengzhimeow.mhdftools.bukkit.util.feature.AtUtil;
import cn.chengzhimeow.mhdftools.bukkit.message.LogUtil;
import cn.chengzhimeow.mhdftools.bukkit.util.teleport.TeleportUtil;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public final class BungeeCordManager {
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

        this.sendPluginMessage(out);
    }

    /**
     * 将指定玩家实例的玩家移动到指定服务器ID的服务器
     *
     * @param player     玩家实例
     * @param serverName 服务器ID
     */
    public void connectServer(OfflinePlayer player, String serverName) {
        this.connectServer(player.getName(), serverName);
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

        this.sendMhdfToolsPluginMessage(data);
    }

    /**
     * 传送指定玩家实例到指定玩家ID的服务器
     *
     * @param player     被传送的玩家实例
     * @param targetName 传送到的玩家ID
     */
    public void teleportPlayer(OfflinePlayer player, String targetName) {
        this.teleportPlayer(player.getName(), targetName);
    }

    /**
     * 传送指定玩家实例到指定玩家ID的服务器
     *
     * @param playerName 被传送的玩家ID
     * @param target     传送到的玩家实例
     */
    public void teleportPlayer(String playerName, OfflinePlayer target) {
        this.teleportPlayer(playerName, target.getName());
    }

    /**
     * 传送指定玩家实例到指定玩家实例的服务器
     *
     * @param player 被传送的玩家实例
     * @param target 传送到的玩家实例
     */
    public void teleportPlayer(OfflinePlayer player, OfflinePlayer target) {
        this.teleportPlayer(player, target.getName());
    }

    /**
     * 将指定玩家ID传送至指定群组位置实例
     *
     * @param playerName         玩家ID
     * @param bungeeCordLocation 群组位置实例
     */
    public void teleportLocation(String playerName, BungeeCordLocation bungeeCordLocation) {
        if (!this.isBungeeCordMode() || bungeeCordLocation.getServer().equals(this.getServerName())) {
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                return;
            }

            player.teleportAsync(bungeeCordLocation.toLocation());
            return;
        }
        Main.instance.getCacheManager().put("tpLocation", playerName, bungeeCordLocation.toString());

        this.connectServer(playerName, bungeeCordLocation.getServer());
    }

    /**
     * 将指定玩家实例传送至指定群组位置实例
     *
     * @param player             玩家实例
     * @param bungeeCordLocation 群组位置实例
     */
    public void teleportLocation(Player player, BungeeCordLocation bungeeCordLocation) {
        this.teleportLocation(player.getName(), bungeeCordLocation);
    }

    /**
     * 向指定玩家ID发送指定文本实例
     *
     * @param playerName 玩家ID
     * @param message    文本实例
     */
    public void sendMessage(String playerName, TextComponent message) {
        if (!this.isBungeeCordMode() && playerName.equals("all")) {
            ActionUtil.broadcastMessage(message);
            return;
        }

        if (!this.isBungeeCordMode() && playerName.equals("console")) {
            LogUtil.log(message.toLegacyString());
            return;
        }

        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            player.sendMessage(message);
            return;
        }

        JSONObject data = new JSONObject();
        data.put("playerName", playerName);
        data.put("message", message.toJsonString());

        ((RedisCacheManager) Main.instance.getCacheManager())
                .getRedisClient()
                .getRedisMessageManager()
                .sendRedisMessage("sendMessage", data.toJSONString());
    }

    /**
     * 向指定玩家实例发送指定消息文本
     *
     * @param player  玩家实例
     * @param message 文本实例
     */
    public void sendMessage(OfflinePlayer player, TextComponent message) {
        this.sendMessage(player.getName(), message);
    }

    /**
     * 向指定玩家ID发送指定文本实例
     *
     * @param message 文本实例
     */
    public void broadcastMessage(TextComponent message) {
        this.sendMessage("all", message);
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
        this.setGameMode(player.getName(), gameMode);
    }

    /**
     * at玩家列表
     *
     * @param atList 玩家列表
     */
    public void atList(Set<String> atList, String by) {
        if (!this.isBungeeCordMode()) {
            AtUtil.atList(atList, by);
            return;
        }

        JSONObject data = new JSONObject();
        data.put("atList", atList);
        data.put("by", by);

        ((RedisCacheManager) Main.instance.getCacheManager())
                .getRedisClient()
                .getRedisMessageManager()
                .sendRedisMessage("atList", data.toJSONString());
    }
}
