package cn.chengzhiya.mhdftools.manager.redis;

import cn.chengzhiya.mhdftools.listener.AbstractRedisMessageListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.Getter;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;

public final class RedisMessageManager {
    @Getter
    private final String prefix;
    private StatefulRedisPubSubConnection<String, String> redisPubSubConnection;

    public RedisMessageManager(String prefix, StatefulRedisPubSubConnection<String, String> redisPubSubConnection) {
        this.prefix = prefix;
        this.redisPubSubConnection = redisPubSubConnection;

        registerListener();
    }

    /**
     * 注册监听器
     */
    @SneakyThrows
    private void registerListener() {
        Reflections reflections = new Reflections(AbstractRedisMessageListener.class.getPackageName());

        for (Class<? extends AbstractRedisMessageListener> clazz : reflections.getSubTypesOf(AbstractRedisMessageListener.class)) {
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                AbstractRedisMessageListener redisMessageListener = clazz.getDeclaredConstructor().newInstance();
                if (redisMessageListener.isEnable()) {
                    this.redisPubSubConnection.async().subscribe(this.getPrefix() + redisMessageListener.getChanel());
                    this.redisPubSubConnection.addListener(redisMessageListener);
                }
            }
        }
    }

    /**
     * 向指定频道发送指定redis消息
     *
     * @param chanel  指定频道
     * @param message 指定消息
     */
    public void sendRedisMessage(String chanel, String message) {
        this.redisPubSubConnection.async().publish(this.getPrefix() + chanel, message);
    }

    /**
     * 关闭redis消息
     */
    public void close() {
        if (this.redisPubSubConnection != null) {
            this.redisPubSubConnection.close();
            this.redisPubSubConnection = null;
        }
    }
}
