package cn.chengzhimeow.mhdftools.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.VanishStatus;

import java.util.List;

public interface VanishStatusManager {
    /**
     * 检测隐身功能是否开启
     *
     * @return 结果
     */
    boolean isEnable();

    /**
     * 设置隐身功能开启状态
     *
     * @param enable 开启状态
     */
    void setEnable(boolean enable);

    /**
     * 获取隐身状态实例列表
     *
     * @return 隐身状态实例列表
     */
    List<VanishStatus> getList();

    /**
     * 检测指定梦之工具玩家实例是否开启影身
     *
     * @param player 梦之工具玩家实例
     * @return 结果
     */
    boolean isEnable(MHDFToolsPlayer player);

    /**
     * 获取指定梦之工具玩家实例的隐身状态实例
     *
     * @param player 梦之工具玩家实例
     * @return 隐身状态实例
     */
    VanishStatus get(MHDFToolsPlayer player);

    /**
     * 更新指定隐身状态实例在数据库中的数据
     *
     * @param vanishStatus 隐身状态实例
     * @param async        异步处理
     */
    void update(VanishStatus vanishStatus, boolean async);

    /**
     * 更新指定隐身状态实例在数据库中的数据
     *
     * @param vanishStatus 隐身状态实例
     */
    void update(VanishStatus vanishStatus);
}
