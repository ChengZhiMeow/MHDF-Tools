package cn.chengzhimeow.mhdftools.bukkit.api.manager;

import java.util.List;

public interface BungeeCordManager {
    /**
     * 检测是否开启群组模式
     *
     * @return 结果
     */
    boolean isBungeeCordMode();

    /**
     * 获取子服在线玩家列表
     *
     * @return 子服在线玩家列表
     */
    List<String> getBukkitPlayerList();

    /**
     * 获取在线玩家列表
     *
     * @return 在线玩家列表
     */
    List<String> getPlayerList();

    /**
     * 判断指定玩家ID的玩家是否在线
     *
     * @param name 玩家ID
     * @return 结果
     */
    boolean ifPlayerOnline(String name);
}
