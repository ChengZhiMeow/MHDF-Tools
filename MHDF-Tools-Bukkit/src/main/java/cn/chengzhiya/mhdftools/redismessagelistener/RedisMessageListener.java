package cn.chengzhiya.mhdftools.redismessagelistener;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.manager.cache.impl.RedisCacheManager;
import cn.chengzhiya.mhdftools.util.config.YamlUtil;
import io.lettuce.core.pubsub.RedisPubSubListener;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class RedisMessageListener implements RedisPubSubListener<String, String> {
    private final boolean enable;
    private final String chanel;

    public RedisMessageListener(List<String> enableKeyList, @NotNull String chanel) {
        this.enable = YamlUtil.equalsTrue(Main.instance.getConfigManager().getConfigManager().getData(), enableKeyList);
        this.chanel = chanel;
    }

    public RedisMessageListener(@NotNull String chanel) {
        this(new ArrayList<>(), chanel);
    }

    /**
     * 当收到redis消息的时候
     *
     * @param message 消息
     */
    abstract public void onMessage(String message);

    @Override
    public void message(String chanel, String message) {
        if (!Objects.equals(chanel, ((RedisCacheManager) Main.instance.getCacheManager())
                .getRedisClient()
                .getRedisMessageManager()
                .getPrefix() + this.chanel)
        ) {
            return;
        }

        this.onMessage(message);
    }

    @Override
    public void message(String chanel, String k1, String string2) {
    }

    @Override
    public void subscribed(String string, long l) {
    }

    @Override
    public void psubscribed(String string, long l) {
    }

    @Override
    public void unsubscribed(String string, long l) {
    }

    @Override
    public void punsubscribed(String string, long l) {
    }
}
