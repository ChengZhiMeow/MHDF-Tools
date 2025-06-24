package cn.chengzhiya.mhdftools.manager.redis;

import cn.chengzhiya.mhdftools.entity.config.RedisConfig;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Getter;

@Getter
public final class RedisClient {
    private final String serverId;

    private final io.lettuce.core.RedisClient redisClient;
    private StatefulRedisConnection<String, String> redisConnection;
    private RedisMessageManager redisMessageManager;

    public RedisClient(String serverId, RedisConfig config) {
        this.serverId = serverId;

        String[] host = config.getHost().split(":");
        RedisURI.Builder uriBuilder = RedisURI.Builder
                .redis(host[0])
                .withPort(Integer.parseInt(host[1]));

        if (config.getUser() != null && !config.getUser().isEmpty()) {
            uriBuilder.withAuthentication(config.getUser(), "");
        }
        if (config.getPassword() != null && !config.getPassword().isEmpty()) {
            uriBuilder.withPassword(config.getPassword().toCharArray());
        }

        this.redisClient = io.lettuce.core.RedisClient.create(uriBuilder.build());
    }

    /**
     * 打开redis连接
     */
    public void connect() {
        this.redisConnection = this.redisClient.connect();
        this.redisMessageManager = new RedisMessageManager(getServerId() + "mhdftools-message-", this.redisClient.connectPubSub());
    }

    /**
     * 关闭redis连接
     */
    public void close() {
        if (this.redisMessageManager != null) {
            this.redisMessageManager.close();
        }
        if (this.redisConnection != null) {
            this.redisConnection.close();
        }
        if (this.redisClient != null) {
            this.redisClient.shutdown();
        }
    }
}
