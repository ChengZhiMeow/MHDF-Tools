package cn.chengzhimeow.mhdftools.bukkit.module.chat.message;

import cn.chengzhimeow.mhdftools.bukkit.module.chat.ModuleMain;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import net.nyana.message.MessageBroker;
import net.nyana.message.executors.MessageExecutors;
import net.nyana.message.libs.codec.Codec;
import net.nyana.message.message.MessageIdentifier;
import net.nyana.message.message.RedisMessage;
import net.nyana.message.util.FriendlyByteBuf;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executor;

public final class ChatPrivateMessage implements RedisMessage {
    public static final MessageIdentifier ID = MessageIdentifier.of("mhdftools", "chat_private");
    public static final Codec<FriendlyByteBuf, ChatPrivateMessage> CODEC = RedisMessage.codec(ChatPrivateMessage::write, ChatPrivateMessage::new);

    private final String sourceServer;
    private final String target;
    private final String miniMessage;
    private final List<byte[]> cacheDataList;

    public ChatPrivateMessage(String sourceServer, String target, String miniMessage, List<byte[]> cacheDataList) {
        this.sourceServer = sourceServer;
        this.target = target;
        this.miniMessage = miniMessage;
        this.cacheDataList = cacheDataList;
    }

    private ChatPrivateMessage(FriendlyByteBuf buf) {
        this.sourceServer = buf.readUtf8();
        this.target = buf.readUtf8();
        this.miniMessage = buf.readUtf8();
        this.cacheDataList = buf.readByteArrayList();
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUtf8(this.sourceServer);
        buf.writeUtf8(this.target);
        buf.writeUtf8(this.miniMessage);
        buf.writeByteArrayList(this.cacheDataList);
    }

    @Override
    public @NotNull MessageIdentifier identifier() {
        return ID;
    }

    @Override
    public void handle(MessageBroker broker) {
        if (broker.brokerId().equals(this.sourceServer)) return;

        Player player = Bukkit.getPlayerExact(this.target);
        if (player == null) return;

        for (byte[] cacheData : this.cacheDataList) {
            ModuleMain.instance.getDisplayCache().putEncoded(cacheData);
        }
        player.sendMessage(ColorUtil.color(this.miniMessage));
    }

    @Override
    public Executor executor() {
        return MessageExecutors.VIRTUAL;
    }
}
