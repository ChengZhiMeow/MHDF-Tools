package cn.chengzhiya.mhdftools.api;

import cn.chengzhiya.mhdftools.api.manager.PlayerManager;
import cn.chengzhiya.mhdftools.api.manager.feature.*;

public interface MHDFToolsAPI {
    /**
     * 获取玩家控制器实例
     *
     * @return 玩家控制器实例
     */
    PlayerManager getPlayerManager();

    /**
     * 获取经济数据控制器实例
     *
     * @return 经济数据控制器实例
     */
    EconomyDataManager getEconomyDataManager();

    /**
     * 获取飞行状态控制器实例
     *
     * @return 飞行状态控制器实例
     */
    FlyStatusManager getFlyStatusManager();

    /**
     * 获取家数据控制器实例
     *
     * @return 家数据控制器实例
     */
    HomeDataManager getHomeDataManager();

    /**
     * 获取忽略数据控制器实例
     *
     * @return 忽略数据控制器实例
     */
    IgnoreDataManager getIgnoreDataManager();

    /**
     * 获取匿名名称数据控制器实例
     *
     * @return 匿名名称数据控制器实例
     */
    NickDataManager getNickDataManager();

    /**
     * 获取玩家数据控制器实例
     *
     * @return 玩家数据控制器实例
     */
    PlayerDataManager getPlayerDataManager();

    /**
     * 获取隐身状态控制器实例
     *
     * @return 隐身状态控制器实例
     */
    VanishStatusManager getVanishStatusManager();

    /**
     * 获取传送点数据控制器实例
     *
     * @return 传送点数据控制器实例
     */
    WarpDataManager getWarpDataManager();

    /**
     * 获取位置记录数据控制器实例
     *
     * @return 位置记录数据控制器实例
     */
    BackDataManager getBackDataManager();

    /**
     * 获取PVP状态控制器实例
     *
     * @return PVP状态控制器实例
     */
    PvpStatusManager getPvpStatusManager();


    /**
     * 获取服务器名称
     *
     * @return 服务器名称
     */
    String getServerName();
}
