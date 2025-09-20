package cn.chengzhimeow.mhdftools.velocity.listener;

import cn.chengzhimeow.mhdftools.velocity.Main;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.Optional;

public final class PluginMessage {
    public static final MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.from("bungeecord:main");

    @Subscribe
    public void onPluginMessageFromPlayer(PluginMessageEvent event) {
        if (!PluginMessage.IDENTIFIER.equals(event.getIdentifier())) {
            return;
        }

        event.setResult(PluginMessageEvent.ForwardResult.handled());

        if (!(event.getSource() instanceof Player sender)) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subchannel = in.readUTF();
        if (!subchannel.equals("mhdf_tools")) {
            return;
        }

        ServerConnection senderServer = sender.getCurrentServer().orElse(null);
        if (senderServer == null) {
            return;
        }

        JSONObject data = JSONObject.parseObject(in.readUTF());
        data.put("from", senderServer.getServerInfo().getName());

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("mhdf_tools");
        out.writeUTF(data.toJSONString());

        String action = data.getString("action");
        JSONObject params = data.getJSONObject("params");
        switch (action) {
            case "teleportPlayer" -> {
                String playerName = params.getString("playerName");
                Player player = Main.instance.getServer().getPlayer(playerName).orElse(null);

                String targetName = params.getString("targetName");
                Player target = Main.instance.getServer().getPlayer(targetName).orElse(null);

                if (player == null || target == null) {
                    return;
                }

                ServerConnection targetServer = target.getCurrentServer().orElse(null);
                if (targetServer == null) {
                    return;
                }

                player.createConnectionRequest(targetServer.getServer());
            }
            default -> {
                String to = data.getString("to");
                switch (to) {
                    case "all" -> {
                        for (RegisteredServer server : Main.instance.getServer().getAllServers()) {
                            this.sendData(server, out);
                        }
                    }
                    case "me" -> this.sendData(senderServer.getServer(), out);
                    default -> {
                        Optional<RegisteredServer> server = Main.instance.getServer().getServer(to);
                        this.sendData(server.orElse(null), out);
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
    private void sendData(RegisteredServer server, ByteArrayDataOutput out) {
        if (server == null) {
            return;
        }

        if (server.getPlayersConnected().isEmpty()) {
            return;
        }

        server.sendPluginMessage(PluginMessage.IDENTIFIER, out.toByteArray());
    }
}
