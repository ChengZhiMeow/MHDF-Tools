package cn.chengzhimeow.mhdftools.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.PlayerData;

public interface PlayerDataManager {
    /**
     * 检测指定梦之工具玩家实例是否拥有玩家数据实例
     *
     * @param player 梦之工具玩家实例
     * @return 结果
     */
    boolean hasData(MHDFToolsPlayer player);

    /**
     * 获取指定梦之工具玩家实例的玩家数据实例
     *
     * @param player 梦之工具玩家实例
     * @return 玩家数据实例
     */
    PlayerData get(MHDFToolsPlayer player);

    /**
     * 获取指定玩家名称的玩家数据实例
     *
     * @param name 玩家名称
     * @return 玩家数据实例
     */
    PlayerData get(String name);

    /**
     * 更新指定玩家数据实例在数据库中的数据
     *
     * @param playerData 玩家数据实例
     * @param async      异步处理
     */
    void update(PlayerData playerData, boolean async);

    /**
     * 更新指定玩家数据实例在数据库中的数据
     *
     * @param playerData 玩家数据实例
     */
    void update(PlayerData playerData);
}
