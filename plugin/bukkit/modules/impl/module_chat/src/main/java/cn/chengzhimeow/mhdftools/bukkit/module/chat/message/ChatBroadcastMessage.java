package cn.chengzhimeow.mhdftools.bukkit.module.chat.message;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.chat.service.AtService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.nyana.message.MessageBroker;
import net.nyana.message.executors.MessageExecutors;
import net.nyana.message.libs.codec.Codec;
import net.nyana.message.message.MessageIdentifier;
import net.nyana.message.message.RedisMessage;
import net.nyana.message.util.FriendlyByteBuf;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executor;

public final class ChatBroadcastMessage implements RedisMessage {
    public static final MessageIdentifier ID = MessageIdentifier.of("mhdftools", "chat_broadcast");
    public static final Codec<FriendlyByteBuf, ChatBroadcastMessage> CODEC = RedisMessage.codec(ChatBroadcastMessage::write, ChatBroadcastMessage::new);

    private final String sourceServer;
    private final String sender;
    private final String messageJson;
    private final List<String> atList;
    private final List<byte[]> cacheDataList;

    public ChatBroadcastMessage(String sourceServer, String sender, String messageJson, List<String> atList, List<byte[]> cacheDataList) {
        this.sourceServer = sourceServer;
        this.sender = sender;
        this.messageJson = messageJson;
        this.atList = atList;
        this.cacheDataList = cacheDataList;
    }

    private ChatBroadcastMessage(FriendlyByteBuf buf) {
        this.sourceServer = buf.readUtf8();
        this.sender = buf.readUtf8();
        this.messageJson = buf.readUtf8();
        this.atList = buf.readStringList();
        this.cacheDataList = buf.readByteArrayList();
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUtf8(this.sourceServer);
        buf.writeUtf8(this.sender);
        buf.writeUtf8(this.messageJson);
        buf.writeStringList(this.atList);
        buf.writeByteArrayList(this.cacheDataList);
    }

    @Override
    public @NotNull MessageIdentifier identifier() {
        return ID;
    }

    @Override
    public void handle(MessageBroker broker) {
        if (BungeeCordManager.getInstance().getServerName().equals(this.sourceServer)) return;

        for (byte[] cacheData : this.cacheDataList) {
            ModuleMain.instance.getDisplayCache().putEncoded(cacheData);
        }

        Component component = GsonComponentSerializer.gson().deserialize(messageJson);
        MHDFToolsPlayer senderPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(this.sender);
        Bukkit.getOnlinePlayers().forEach(player -> {
            MHDFToolsPlayer targetPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
            if (targetPlayer.isIgnore(senderPlayer)) return;
            player.sendMessage(component);
            AtService.at(player, this.sender, this.atList);
        });
        Bukkit.getConsoleSender().sendMessage(component);
    }

    @Override
    public Executor executor() {
        return MessageExecutors.VIRTUAL;
    }
}
