package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.FlyStatus;

import java.util.List;

public interface FlyStatusManager {
    /**
     * 获取飞行状态实例列表
     *
     * @return 飞行状态实例列表
     */
    List<FlyStatus> getList();

    /**
     * 检测指定梦之工具玩家实例是否开启飞行
     *
     * @param player 梦之工具玩家实例
     * @return 结果
     */
    boolean isEnable(MHDFToolsPlayer player);

    /**
     * 获取指定梦之工具玩家实例的飞行状态实例
     *
     * @param player 梦之工具玩家实例
     * @return 飞行状态实例
     */
    FlyStatus get(MHDFToolsPlayer player);

    /**
     * 更新指定飞行状态实例在数据库中的数据
     *
     * @param flyStatus 飞行状态实例
     * @param async  异步处理
     */
    void update(FlyStatus flyStatus, boolean async);

    /**
     * 更新指定飞行状态实例在数据库中的数据
     *
     * @param flyStatus 飞行状态实例
     */
    void update(FlyStatus flyStatus);
}
