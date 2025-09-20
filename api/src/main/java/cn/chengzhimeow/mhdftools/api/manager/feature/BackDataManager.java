package cn.chengzhimeow.mhdftools.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.BackData;

import java.util.List;

public interface BackDataManager {
    /**
     * 获取位置记录数据实例列表
     *
     * @return 位置记录数据实例列表
     */
    List<BackData> getList();

    /**
     * 获取位置记录数据实例列表
     *
     * @param player 梦之工具玩家实例
     * @param amount 数量
     * @return 位置记录数据实例列表
     */
    List<BackData> getList(MHDFToolsPlayer player, int amount);

    /**
     * 获取位置记录数据实例列表
     *
     * @param player 梦之工具玩家实例
     * @param type   类型
     * @param amount 数量
     * @return 位置记录数据实例列表
     */
    List<BackData> getList(MHDFToolsPlayer player, String type, int amount);

    /**
     * 获取位置记录数据实例
     *
     * @param id 位置记录数据ID
     * @return 位置记录数据实例
     */
    BackData getById(Integer id);

    /**
     * 更新指定位置记录数据实例在数据库中的数据
     *
     * @param backData 位置记录数据实例
     * @param async    异步处理
     */
    void update(BackData backData, boolean async);

    /**
     * 更新指定位置记录数据实例在数据库中的数据
     *
     * @param backData 位置记录数据实例
     */
    void update(BackData backData);
}
