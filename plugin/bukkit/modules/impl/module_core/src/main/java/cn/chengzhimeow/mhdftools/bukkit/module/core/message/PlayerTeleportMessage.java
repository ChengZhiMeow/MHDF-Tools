package cn.chengzhimeow.mhdftools.bukkit.module.core.message;

import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.module.core.ModuleMain;
import cn.chengzhimeow.mhdftools.thread.ThreadPool;
import net.nyana.message.MessageBroker;
import net.nyana.message.executors.MessageExecutors;
import net.nyana.message.libs.codec.Codec;
import net.nyana.message.message.MessageIdentifier;
import net.nyana.message.message.RedisMessage;
import net.nyana.message.util.FriendlyByteBuf;
import net.nyana.nbt.NBT;
import net.nyana.nbt.tag.CompoundTag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.Executor;

public final class PlayerTeleportMessage implements RedisMessage {
    public static final MessageIdentifier ID = MessageIdentifier.of("mhdftools", "player_teleport");
    public static final Codec<FriendlyByteBuf, PlayerTeleportMessage> CODEC = RedisMessage.codec(PlayerTeleportMessage::write, PlayerTeleportMessage::new);
    private static final ThreadPool thread = new ThreadPool(1, "MHDF-Tools PlayerTeleport Message Thread");

    private final String sourceServer;
    private final String player;
    private final String target;

    public PlayerTeleportMessage(String sourceServer, String player, String target) {
        this.sourceServer = sourceServer;
        this.player = player;
        this.target = target;
    }

    private PlayerTeleportMessage(FriendlyByteBuf buf) {
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
        BungeeCordManager bungeeCordManager = BungeeCordManager.getInstance();
        if (bungeeCordManager == null || bungeeCordManager.getServerName().equals(this.sourceServer)) return;

        Player target = Bukkit.getPlayerExact(this.target);
        if (target == null || !target.isOnline()) return;

        String targetServer = bungeeCordManager.getServerName();
        thread.execute(() -> {
            if (!this.writeTeleportCache()) return;
            bungeeCordManager.connectServer(this.player, targetServer);
        });
    }

    private boolean writeTeleportCache() {
        try {
            CompoundTag root = NBT.createCompound();
            CompoundTag info = NBT.createCompound();
            root.putByte("mode", (byte) 0);
            info.putString("player", this.target);
            root.put("info", info);

            ModuleMain.instance.serverTeleportCache.put(this.player, NBT.toBytes(root));
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    @Override
    public Executor executor() {
        return MessageExecutors.VIRTUAL;
    }
}
