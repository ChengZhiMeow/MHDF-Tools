package cn.chengzhimeow.mhdftools.bukkit.module.broadcast.message;

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.nyana.message.MessageBroker;
import net.nyana.message.executors.MessageExecutors;
import net.nyana.message.libs.codec.Codec;
import net.nyana.message.message.MessageIdentifier;
import net.nyana.message.message.RedisMessage;
import net.nyana.message.util.FriendlyByteBuf;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public final class BroadcastMessage implements RedisMessage {
    public static final MessageIdentifier ID = MessageIdentifier.of("mhdftools", "broadcast");
    public static final Codec<FriendlyByteBuf, BroadcastMessage> CODEC = RedisMessage.codec(BroadcastMessage::write, BroadcastMessage::new);

    private final String messageJson;

    public BroadcastMessage(String messageJson) {
        this.messageJson = messageJson;
    }

    private BroadcastMessage(FriendlyByteBuf buf) {
        this.messageJson = buf.readUtf8();
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUtf8(this.messageJson);
    }

    @Override
    public @NotNull MessageIdentifier identifier() {
        return ID;
    }

    @Override
    public void handle(MessageBroker broker) {
        Bukkit.getConsoleSender().sendMessage(GsonComponentSerializer.gson().deserialize(messageJson));
    }

    @Override
    public Executor executor() {
        return MessageExecutors.VIRTUAL;
    }
}
