package cn.chengzhimeow.mhdftools.bukkit.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class BigDecimalUtil {
    /**
     * 将数值转换为大数值对象实例
     *
     * @param value 数值
     * @return 大数值对象实例
     */
    public static BigDecimal toBigDecimal(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}
