package cn.chengzhimeow.mhdftools.bukkit.api.redis;

import net.nyana.message.libs.codec.Codec;
import net.nyana.message.message.MessageIdentifier;
import net.nyana.message.message.RedisMessage;
import net.nyana.message.util.FriendlyByteBuf;

public abstract class RedisManager {
    protected RedisManager() {
    }

    public abstract <T extends RedisMessage> void register(MessageIdentifier identifier, Codec<FriendlyByteBuf, T> codec);

    public abstract boolean publish(RedisMessage message);

    public abstract boolean isOpen();

    public abstract void close();
}
