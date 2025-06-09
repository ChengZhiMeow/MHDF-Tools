package cn.chengzhiya.mhdftools.listener;

import cn.chengzhiya.mhdftools.Main;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.SneakyThrows;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.InetAddress;

public final class PluginMessage implements Listener {
    /**
     * 通过插件消息事件实例获取发送端服务器实例
     *
     * @param event 插件消息事件实例
     * @return 发送端服务器实例
     */
    public ServerInfo getServerInfo(PluginMessageEvent event) {
        InetAddress senderAddress = event.getSender().getAddress().getAddress();
        int senderPort = event.getSender().getAddress().getPort();

        for (ServerInfo server : Main.instance.getProxy().getServers().values()) {
            InetAddress serverAddress = server.getAddress().getAddress();

            int serverPort = server.getAddress().getPort();

            if (serverAddress.equals(senderAddress) && serverPort == senderPort) {
                return server;
            }
        }
        return null;
    }

    @EventHandler
    @SneakyThrows
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().contains("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
            String subchannel = in.readUTF();
            if (!subchannel.equals("mhdf_tools")) {
                return;
            }

            ServerInfo senderServer = getServerInfo(event);
            if (senderServer == null) {
                return;
            }

            JSONObject data = JSONObject.parseObject(in.readUTF());
            data.put("from", senderServer.getName());

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("mhdf_tools");
            out.writeUTF(data.toJSONString());

            String action = data.getString("action");
            JSONObject params = data.getJSONObject("params");
            switch (action) {
                case "teleportPlayer" -> {
                    String playerName = params.getString("playerName");
                    ProxiedPlayer player = Main.instance.getProxy().getPlayer(playerName);

                    String targetName = params.getString("targetName");
                    ProxiedPlayer target = Main.instance.getProxy().getPlayer(targetName);

                    if (player == null || target == null) {
                        return;
                    }

                    player.connect(target.getServer().getInfo());
                }
                default -> {
                    String to = data.getString("to");
                    switch (to) {
                        case "all" -> {
                            for (ServerInfo server : Main.instance.getProxy().getServers().values()) {
                                sendData(server, out);
                            }
                        }
                        case "me" -> sendData(senderServer, out);
                        default -> {
                            ServerInfo server = Main.instance.getProxy().getServerInfo(to);
                            sendData(server, out);
                        }
                    }
                }
            }
    }

    /**
     * 发送插件消息给指定服务器实例
     *
     * @param server 服务器实例
     * @param out    消息数据实例
     */
    private void sendData(ServerInfo server, ByteArrayDataOutput out) {
        if (server == null) {
            return;
        }

        if (server.getPlayers().isEmpty()) {
            return;
        }

        server.sendData("BungeeCord", out.toByteArray());
    }
}
