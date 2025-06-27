package cn.chengzhiya.mhdftools.placeholder;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public interface Placeholder {
    /**
     * 获取指定玩家实例指定变量的值
     *
     * @param player      玩家实例
     * @param placeholder 变量
     * @return 指定玩家实例下指定变量的值
     */
    String placeholder(OfflinePlayer player, @NotNull String placeholder);
}
