package cn.chengzhimeow.mhdftools.bukkit.api.redis;

import cn.chengzhimeow.mhdftools.bukkit.common.config.CacheSetting;
import net.nyana.message.Logger;
import net.nyana.message.MessageBroker;
import net.nyana.message.connection.DefaultRedisConnection;
import net.nyana.message.libs.codec.Codec;
import net.nyana.message.message.MessageIdentifier;
import net.nyana.message.message.RedisMessage;
import net.nyana.message.util.FriendlyByteBuf;
import org.bukkit.Bukkit;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public final class RedisManagerImpl extends RedisManager {
    private final Set<Registration<?>> registrations = new LinkedHashSet<>();
    private MessageBroker broker;
    private String serverId = "local";

    public synchronized void configure() {
        CacheSetting.Config settings = CacheSetting.getInstance().getConfig();
        this.serverId = settings.server() == null || settings.server().isBlank() ? "local" : settings.server();
        if (!settings.isRedis() || this.broker != null) return;

        this.broker = MessageBroker.builder()
                .appId("mhdftools:" + this.serverId)
                .brokerId(UUID.randomUUID().toString())
                .logger(new BukkitRedisLogger())
                .connection(new DefaultRedisConnection(settings.redis().uri(), 1_000_000))
                .build();
        for (Registration<?> registration : this.registrations) {
            registration.register(this.broker);
        }
        this.broker.subscribe();
    }

    @Override
    public synchronized <T extends RedisMessage> void register(MessageIdentifier identifier, Codec<FriendlyByteBuf, T> codec) {
        Registration<T> registration = new Registration<>(identifier, codec);
        this.registrations.add(registration);
        if (this.broker != null) {
            registration.register(this.broker);
        }
    }

    @Override
    public synchronized boolean publish(RedisMessage message) {
        if (this.broker == null) return false;

        this.broker.publish(message);
        return true;
    }

    @Override
    public synchronized boolean isOpen() {
        return this.broker != null;
    }

    @Override
    public synchronized void close() {
        if (this.broker == null) return;

        this.broker.unsubscribe();
        this.broker.connection().close();
        this.broker = null;
    }

    private record Registration<T extends RedisMessage>(MessageIdentifier identifier, Codec<FriendlyByteBuf, T> codec) {
        private void register(MessageBroker broker) {
            broker.registry().register(this.identifier, this.codec);
        }
    }

    private static final class BukkitRedisLogger implements Logger {
        @Override
        public void error(String message, Throwable throwable) {
            Bukkit.getLogger().log(Level.SEVERE, message, throwable);
        }

        @Override
        public void warn(String message, Throwable throwable) {
            Bukkit.getLogger().log(Level.WARNING, message, throwable);
        }

        @Override
        public void info(String message) {
            Bukkit.getLogger().info(message);
        }

        @Override
        public void debug(String message) {
            Bukkit.getLogger().fine(message);
        }
    }
}
