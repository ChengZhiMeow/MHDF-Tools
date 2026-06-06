package cn.chengzhimeow.mhdftools.bukkit.util.math;

import io.netty.util.internal.ThreadLocalRandom;

public final class RandomUtil {
    /**
     * 生存随机整数（包含最小值，不包含最大值）
     *
     * @param min 最小值
     * @param max 最大值
     * @return 结果
     */
    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
