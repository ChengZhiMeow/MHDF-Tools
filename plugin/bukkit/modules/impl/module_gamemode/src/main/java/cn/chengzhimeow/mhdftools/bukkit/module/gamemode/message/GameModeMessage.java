package cn.chengzhimeow.mhdftools.bukkit.module.gamemode.message;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import net.nyana.message.MessageBroker;
import net.nyana.message.executors.MessageExecutors;
import net.nyana.message.libs.codec.Codec;
import net.nyana.message.message.MessageIdentifier;
import net.nyana.message.message.RedisMessage;
import net.nyana.message.util.FriendlyByteBuf;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public final class GameModeMessage implements RedisMessage {
    public static final MessageIdentifier ID = MessageIdentifier.of("mhdftools", "game_mode");
    public static final Codec<FriendlyByteBuf, GameModeMessage> CODEC = RedisMessage.codec(GameModeMessage::write, GameModeMessage::new);

    private final String sourceServer;
    private final String target;
    private final String gameMode;

    public GameModeMessage(String sourceServer, String target, String gameMode) {
        this.sourceServer = sourceServer;
        this.target = target;
        this.gameMode = gameMode;
    }

    private GameModeMessage(FriendlyByteBuf buf) {
        this.sourceServer = buf.readUtf8();
        this.target = buf.readUtf8();
        this.gameMode = buf.readUtf8();
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUtf8(this.sourceServer);
        buf.writeUtf8(this.target);
        buf.writeUtf8(this.gameMode);
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

        GameMode gameMode = GameMode.valueOf(this.gameMode);
        CCScheduler.getInstance().getEntityScheduler().runTask(MHDFToolsBukkit.getInstance(), player, () -> player.setGameMode(gameMode));
    }

    @Override
    public Executor executor() {
        return MessageExecutors.VIRTUAL;
    }
}
