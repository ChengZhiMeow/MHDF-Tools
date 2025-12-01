package cn.chengzhimeow.mhdftools.api;

import cn.chengzhimeow.mhdftools.api.manager.PlayerManager;
import cn.chengzhimeow.mhdftools.api.manager.feature.*;
import lombok.Getter;
import lombok.Setter;

public abstract class MHDFToolsAPI {
    @Getter
    @Setter
    private static MHDFToolsAPI instance;

    /**
     * 获取玩家控制器实例
     *
     * @return 玩家控制器实例
     */
    public abstract PlayerManager getPlayerManager();

    /**
     * 获取经济数据控制器实例
     *
     * @return 经济数据控制器实例
     */
    public abstract EconomyDataManager getEconomyDataManager();

    /**
     * 获取飞行状态控制器实例
     *
     * @return 飞行状态控制器实例
     */
    public abstract FlyStatusManager getFlyStatusManager();

    /**
     * 获取家数据控制器实例
     *
     * @return 家数据控制器实例
     */
    public abstract HomeDataManager getHomeDataManager();

    /**
     * 获取忽略数据控制器实例
     *
     * @return 忽略数据控制器实例
     */
    public abstract IgnoreDataManager getIgnoreDataManager();

    /**
     * 获取匿名名称数据控制器实例
     *
     * @return 匿名名称数据控制器实例
     */
    public abstract NickDataManager getNickDataManager();

    /**
     * 获取玩家数据控制器实例
     *
     * @return 玩家数据控制器实例
     */
    public abstract PlayerDataManager getPlayerDataManager();

    /**
     * 获取隐身状态控制器实例
     *
     * @return 隐身状态控制器实例
     */
    public abstract VanishStatusManager getVanishStatusManager();

    /**
     * 获取传送点数据控制器实例
     *
     * @return 传送点数据控制器实例
     */
    public abstract WarpDataManager getWarpDataManager();

    /**
     * 获取位置记录数据控制器实例
     *
     * @return 位置记录数据控制器实例
     */
    public abstract BackDataManager getBackDataManager();

    /**
     * 获取PVP状态控制器实例
     *
     * @return PVP状态控制器实例
     */
    public abstract PvpStatusManager getPvpStatusManager();


    /**
     * 获取服务器名称
     *
     * @return 服务器名称
     */
    public abstract String getServerName();
}
