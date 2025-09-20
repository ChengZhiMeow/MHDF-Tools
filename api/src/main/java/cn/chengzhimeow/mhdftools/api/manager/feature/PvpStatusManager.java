package cn.chengzhimeow.mhdftools.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.PvpStatus;

import java.util.List;

public interface PvpStatusManager {
    /**
     * 获取pvp状态实例列表
     *
     * @return pvp状态实例列表
     */
    List<PvpStatus> getList();

    /**
     * 检测指定梦之工具玩家实例是否开启影身
     *
     * @param player 梦之工具玩家实例
     * @return 结果
     */
    boolean isEnable(MHDFToolsPlayer player);

    /**
     * 获取指定梦之工具玩家实例的pvp状态实例
     *
     * @param player 梦之工具玩家实例
     * @return pvp状态实例
     */
    PvpStatus get(MHDFToolsPlayer player);

    /**
     * 更新指定pvp状态实例在数据库中的数据
     *
     * @param pvpStatus pvp状态实例
     * @param async     异步处理
     */
    void update(PvpStatus pvpStatus, boolean async);

    /**
     * 更新指定pvp状态实例在数据库中的数据
     *
     * @param pvpStatus pvp状态实例
     */
    void update(PvpStatus pvpStatus);

    /**
     * 获取默认值
     *
     * @return 默认值
     */
    boolean getDefaultValue();
}
