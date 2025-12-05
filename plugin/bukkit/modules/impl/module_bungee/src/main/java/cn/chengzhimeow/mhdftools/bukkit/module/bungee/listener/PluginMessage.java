package cn.chengzhimeow.mhdftools.bukkit.module.bungee.listener;

import cn.chengzhimeow.mhdftools.bukkit.module.bungee.manager.BungeeCordManagerImpl;
import cn.chengzhimeow.mhdftools.console.LogManager;
import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.List;

public final class PluginMessage implements PluginMessageListener {
    @Override
    @SneakyThrows
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player messagePlayer, byte[] messageData) {
        if (!channel.equals("BungeeCord")) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(messageData));
        String subchannel = in.readUTF();

        switch (subchannel) {
            case "mhdf_tools" -> {
                JSONObject data = JSONObject.parseObject(in.readUTF());

                LogManager.instance.debug("收到来自群组端的梦之工具消息 | 消息: {}", data.toJSONString());

                String action = data.getString("action");
                String from = data.getString("from");
                String to = data.getString("to");
                JSONObject params = data.getJSONObject("params");

                switch (action) {
                    case "server_info" -> {
                        LogManager.instance.debug("更新服务器名称 | 名称: {}", from);
                        BungeeCordManagerImpl.getInstance().setServerName(from);
                    }
                }
            }
            case "PlayerList" -> {
                in.readUTF();
                String playerListString = in.readUTF();

                LogManager.instance.debug("更新在线玩家列表 | 在线列表: {}", playerListString);

                List<String> playerList = List.of(playerListString.split(", "));
                BungeeCordManagerImpl.getInstance().setPlayerList(playerList);
            }
        }
    }
}

