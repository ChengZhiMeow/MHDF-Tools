package cn.chengzhiya.mhdftools.redismessagelistener;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.manager.cache.MHDFCacheManager;
import cn.chengzhiya.mhdftools.manager.cache.impl.RedisCacheManager;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.YamlUtil;
import io.lettuce.core.pubsub.RedisPubSubListener;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class AbstractRedisMessageListener implements RedisPubSubListener<String, String>, RedisMessageListener {
    private final boolean enable;
    private final String chanel;

    public AbstractRedisMessageListener(List<String> enableKeyList, @NotNull String chanel) {
        this.enable = YamlUtil.equalsTrue(ConfigUtil.getConfig(), enableKeyList);
        this.chanel = chanel;
    }

    public AbstractRedisMessageListener(@NotNull String chanel) {
        this(new ArrayList<>(), chanel);
    }

    @Override
    public void message(String chanel, String message) {
        if (!Objects.equals(chanel, ((RedisCacheManager) ((MHDFCacheManager) Main.instance.getCacheManager()).getCacheManager())
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
