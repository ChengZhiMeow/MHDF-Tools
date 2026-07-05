package cn.chengzhimeow.mhdftools.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.database.data.PlayerData;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PlayerDataManager {
    /**
     * 检测指定梦玩家UUID是否拥有玩家数据实例
     *
     * @param uuid 玩家UUID
     * @return 结果
     */
    boolean hasData(UUID uuid);

    /**
     * 获取指定玩家UUID的玩家数据实例
     *
     * @param uuid 玩家UUID
     * @return 玩家数据实例
     */
    PlayerData get(UUID uuid);

    /**
     * 获取指定玩家名称的玩家数据实例
     *
     * @param name 玩家名称
     * @return 玩家数据实例
     */
    PlayerData get(String name);

    /**
     * 获取指定玩家名称的玩家数据实例
     *
     * @param name 玩家名称
     * @return 玩家数据实例
     */
    @Nullable PlayerData getOrNull(String name);

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
