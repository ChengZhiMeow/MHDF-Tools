package cn.chengzhimeow.mhdftools.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.EconomyData;

public interface EconomyDataManager {
    /**
     * 检测经济功能是否开启
     *
     * @return 结果
     */
    boolean isEnable();

    /**
     * 设置经济功能开启状态
     *
     * @param enable 开启状态
     */
    void setEnable(boolean enable);

    /**
     * 获取货币名称
     *
     * @return 货币名称
     */
    String getMoneyName();

    /**
     * 检测指定梦之工具玩家实例是否拥有经济数据
     *
     * @param player 梦之工具玩家实例
     * @return 结果
     */
    boolean hasData(MHDFToolsPlayer player);

    /**
     * 获取指定梦之工具玩家实例的经济数据实例
     *
     * @param player 梦之工具玩家实例
     * @return 经济数据实例
     */
    EconomyData get(MHDFToolsPlayer player);

    /**
     * 更新指定经济数据实例在数据库中的数据
     *
     * @param economyData 经济数据实例
     * @param async       异步处理
     */
    void update(EconomyData economyData, boolean async);

    /**
     * 更新指定经济数据实例在数据库中的数据
     *
     * @param economyData 经济数据实例
     */
    void update(EconomyData economyData);
}
