package cn.chengzhimeow.mhdftools.bukkit.module.core.message;

import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.nyana.message.MessageBroker;
import net.nyana.message.executors.MessageExecutors;
import net.nyana.message.libs.codec.Codec;
import net.nyana.message.message.MessageIdentifier;
import net.nyana.message.message.RedisMessage;
import net.nyana.message.util.FriendlyByteBuf;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public final class PlayerMessage implements RedisMessage {
    public static final MessageIdentifier ID = MessageIdentifier.of("mhdftools", "player_message");
    public static final Codec<FriendlyByteBuf, PlayerMessage> CODEC = RedisMessage.codec(PlayerMessage::write, PlayerMessage::new);

    private final String sourceServer;
    private final String target;
    private final String messageJson;

    public PlayerMessage(String sourceServer, String target, String messageJson) {
        this.sourceServer = sourceServer;
        this.target = target;
        this.messageJson = messageJson;
    }

    private PlayerMessage(FriendlyByteBuf buf) {
        this.sourceServer = buf.readUtf8();
        this.target = buf.readUtf8();
        this.messageJson = buf.readUtf8();
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUtf8(this.sourceServer);
        buf.writeUtf8(this.target);
        buf.writeUtf8(this.messageJson);
    }

    @Override
    public @NotNull MessageIdentifier identifier() {
        return ID;
    }

    @Override
    public void handle(MessageBroker broker) {
        BungeeCordManager bungeeCordManager = BungeeCordManager.getInstance();
        if (bungeeCordManager != null && bungeeCordManager.getServerName().equals(sourceServer)) return;

        Player player = Bukkit.getPlayerExact(target);
        if (player == null) return;

        player.sendMessage(GsonComponentSerializer.gson().deserialize(messageJson));
    }

    @Override
    public Executor executor() {
        return MessageExecutors.VIRTUAL;
    }
}
