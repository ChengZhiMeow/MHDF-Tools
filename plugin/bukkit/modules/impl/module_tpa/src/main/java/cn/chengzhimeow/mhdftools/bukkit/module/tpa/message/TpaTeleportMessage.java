package cn.chengzhimeow.mhdftools.bukkit.module.tpa.message;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
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

public final class TpaTeleportMessage implements RedisMessage {
    public static final MessageIdentifier ID = MessageIdentifier.of("mhdftools", "tpa_teleport");
    public static final Codec<FriendlyByteBuf, TpaTeleportMessage> CODEC = RedisMessage.codec(TpaTeleportMessage::write, TpaTeleportMessage::new);

    private final String sourceServer;
    private final String player;
    private final String target;

    public TpaTeleportMessage(String sourceServer, String player, String target) {
        this.sourceServer = sourceServer;
        this.player = player;
        this.target = target;
    }

    private TpaTeleportMessage(FriendlyByteBuf buf) {
        this.sourceServer = buf.readUtf8();
        this.player = buf.readUtf8();
        this.target = buf.readUtf8();
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUtf8(this.sourceServer);
        buf.writeUtf8(this.player);
        buf.writeUtf8(this.target);
    }

    @Override
    public @NotNull MessageIdentifier identifier() {
        return ID;
    }

    @Override
    public void handle(MessageBroker broker) {
        if (BungeeCordManager.getInstance().getServerName().equals(this.sourceServer)) return;

        Player player = Bukkit.getPlayerExact(this.player);
        if (player == null) return;

        MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
        MHDFToolsPlayer mhdfTarget = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(this.target);
        mhdfPlayer.teleport(mhdfTarget);
    }

    @Override
    public Executor executor() {
        return MessageExecutors.VIRTUAL;
    }
}
