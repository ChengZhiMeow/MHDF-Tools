package cn.chengzhimeow.mhdftools.bukkit.util.action;

import cn.chengzhimeow.mhdftools.bukkit.util.message.ColorUtil;
import net.kyori.adventure.bossbar.BossBar;

@SuppressWarnings("unused")
public final class BossBarUtil {
    /**
     * 构建boss血条实例
     *
     * @param title 标题
     * @param color 颜色
     * @param style 样式
     * @return boss血条实例
     */
    public static BossBar getBossBar(String title, BossBar.Color color, BossBar.Overlay style) {
        return BossBar.bossBar(ColorUtil.color(title), 1f, color, style);
    }

    /**
     * 构建boss血条实例
     *
     * @param title 标题
     * @param color 颜色
     * @return boss血条实例
     */
    public static BossBar getBossBar(String title, BossBar.Color color) {
        return BossBarUtil.getBossBar(title, color, BossBar.Overlay.PROGRESS);
    }

    /**
     * 构建boss血条实例
     *
     * @param title 标题
     * @return boss血条实例
     */
    public static BossBar getBossBar(String title) {
        return BossBarUtil.getBossBar(title, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS);
    }
}
