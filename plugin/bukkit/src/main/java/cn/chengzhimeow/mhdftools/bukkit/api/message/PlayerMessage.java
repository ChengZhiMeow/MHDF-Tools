package cn.chengzhimeow.mhdftools.bukkit.api.message;

import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
    private final String miniMessage;

    public PlayerMessage(String sourceServer, String target, String miniMessage) {
        this.sourceServer = sourceServer;
        this.target = target;
        this.miniMessage = miniMessage;
    }

    private PlayerMessage(FriendlyByteBuf buf) {
        this.sourceServer = buf.readUtf8();
        this.target = buf.readUtf8();
        this.miniMessage = buf.readUtf8();
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUtf8(this.sourceServer);
        buf.writeUtf8(this.target);
        buf.writeUtf8(this.miniMessage);
    }

    @Override
    public @NotNull MessageIdentifier identifier() {
        return ID;
    }

    @Override
    public void handle(MessageBroker broker) {
        if (BungeeCordManager.getInstance().getServerName().equals(this.sourceServer)) return;

        Player player = Bukkit.getPlayerExact(this.target);
        if (player == null) return;

        Bukkit.getScheduler().runTask(MHDFToolsBukkit.getInstance(), () -> player.sendMessage(MiniMessage.miniMessage().deserialize(this.miniMessage)));
    }

    @Override
    public Executor executor() {
        return MessageExecutors.VIRTUAL;
    }
}
