package cn.chengzhimeow.mhdftools.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.NickData;

public interface NickDataManager {
    /**
     * 检测指定梦之工具玩家实例是否拥有匿名数据
     *
     * @param player 梦之工具玩家实例
     * @return 结果
     */
    boolean hasData(MHDFToolsPlayer player);

    /**
     * 获取指定梦之工具玩家实例的匿名数据实例
     *
     * @param player 梦之工具玩家实例
     * @return 匿名数据实例
     */
    NickData get(MHDFToolsPlayer player);

    /**
     * 删除指定匿名数据实例在数据库中的数据
     *
     * @param nickData 匿名数据实例
     * @param async    异步处理
     */
    void delete(NickData nickData, boolean async);

    /**
     * 删除指定匿名数据实例在数据库中的数据
     *
     * @param nickData 匿名数据实例
     */
    void delete(NickData nickData);

    /**
     * 更新指定匿名数据实例在数据库中的数据
     *
     * @param nickData 匿名数据实例
     * @param async    异步处理
     */
    void update(NickData nickData, boolean async);

    /**
     * 更新指定匿名数据实例在数据库中的数据
     *
     * @param nickData 匿名数据实例
     */
    void update(NickData nickData);
}
