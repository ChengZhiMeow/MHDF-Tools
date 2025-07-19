package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdftools.api.entity.database.data.WarpData;

import java.util.List;

public interface WarpDataManager {
    /**
     * 获取传送点数据实例列表
     *
     * @return 传送点数据实例列表
     */
    List<WarpData> getList();

    /**
     * 检测是否存在指定名称的传送点
     *
     * @param name 传动点名称
     * @return 结果
     */
    boolean hasData(String name);

    /**
     * 获取指定名称的传送点数据实例
     *
     * @param name 传送点名称
     * @return 传送点数据实例
     */
    WarpData get(String name);

    /**
     * 删除指定传送点数据实例在数据库中的数据
     *
     * @param warpData 传送点数据实例
     * @param async    异步处理
     */
    void delete(WarpData warpData, boolean async);

    /**
     * 删除指定传送点数据实例在数据库中的数据
     *
     * @param warpData 传送点数据实例
     */
    void delete(WarpData warpData);

    /**
     * 更新指定传送点数据实例在数据库中的数据
     *
     * @param warpData 传送点数据实例
     * @param async    异步处理
     */
    void update(WarpData warpData, boolean async);

    /**
     * 更新指定传送点数据实例在数据库中的数据
     *
     * @param warpData 传送点数据实例
     */
    void update(WarpData warpData);
}
