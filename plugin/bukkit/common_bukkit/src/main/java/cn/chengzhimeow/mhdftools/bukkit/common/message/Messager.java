package cn.chengzhimeow.mhdftools.bukkit.common.message;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import net.nyana.message.message.RedisMessage;

public final class Messager {
    private Messager() {
    }

    public static void send(RedisMessage message, Runnable local) {
        local.run();
        Messager.publish(message);
    }

    public static void publish(RedisMessage message) {
        MHDFToolsBukkit.getInstance().getRedisManager().publish(message);
    }
}
