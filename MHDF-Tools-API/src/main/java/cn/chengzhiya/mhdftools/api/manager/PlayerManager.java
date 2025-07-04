package cn.chengzhiya.mhdftools.api.manager;

import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface PlayerManager {
    /**
     * 获取指定玩家UUID的梦之工具玩家实例
     *
     * @param uuid 玩家UUID
     * @return 梦之工具玩家实例
     */
    MHDFToolsPlayer getPlayer(UUID uuid);

    /**
     * 获取指定玩家名称的梦之工具玩家实例
     *
     * @param name 玩家名称
     * @return 梦之工具玩家实例
     */
    MHDFToolsPlayer getPlayer(String name);

    /**
     * 获取指定玩家实例的梦之工具玩家实例
     *
     * @param player 玩家实例
     * @return 梦之工具玩家实例
     */
    MHDFToolsPlayer getPlayer(OfflinePlayer player);
}
