package cn.chengzhimeow.mhdftools.bukkit.common.bungee;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public abstract class BungeeCordManager {
    @Getter
    @Setter
    private static BungeeCordManager instance;

    /**
     * 检测是否开启群组模式
     *
     * @return 结果
     */
    public abstract boolean isBungeeCordMode();

    /**
     * 获取服务器群组名称
     *
     * @return 群组名称
     */
    public abstract String getServerName();

    /**
     * 获取子服在线玩家列表
     *
     * @return 子服在线玩家列表
     */
    public abstract List<String> getBukkitPlayerList();

    /**
     * 获取在线玩家列表
     *
     * @return 在线玩家列表
     */
    public abstract List<String> getPlayerList();

    /**
     * 判断指定玩家ID的玩家是否在线
     *
     * @param name 玩家ID
     * @return 结果
     */
    public abstract boolean ifPlayerOnline(String name);

    /**
     * 让指定玩家ID的玩家前往指定服务器
     *
     * @param name       玩家ID
     * @param serverName 服务器ID
     */
    public abstract void connectServer(String name, String serverName);
}
