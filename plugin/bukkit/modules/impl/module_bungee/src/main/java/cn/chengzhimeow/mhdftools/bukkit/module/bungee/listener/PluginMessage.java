package cn.chengzhimeow.mhdftools.bukkit.module.bungee.listener;

import cn.chengzhimeow.mhdftools.bukkit.module.bungee.manager.BungeeCordManagerImpl;
import cn.chengzhimeow.mhdftools.console.LogManager;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Set;

public final class PluginMessage implements PluginMessageListener {
    @Override
    @SneakyThrows
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player messagePlayer, byte[] messageData) {
        if (!channel.equals("BungeeCord")) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(messageData));
        String subchannel = in.readUTF();

        switch (subchannel) {
            case "GetServer" -> {
                String serverName = in.readUTF();
                LogManager.instance.debug("更新服务器名称 | 名称: {}", serverName);
                BungeeCordManagerImpl.getInstance().setServerName(serverName);
            }
            case "PlayerList" -> {
                in.readUTF();
                String playerListString = in.readUTF();

                LogManager.instance.debug("更新在线玩家列表 | 在线列表: {}", playerListString);

                Set<String> playerList = Set.of(playerListString.split(", "));
                BungeeCordManagerImpl.getInstance().setPlayerList(playerList);
            }
        }
    }
}

