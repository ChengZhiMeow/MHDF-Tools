package cn.chengzhimeow.mhdftools.api.manager.feature;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.IgnoreData;

import java.util.List;

public interface IgnoreDataManager {
    /**
     * 检测屏蔽功能是否开启
     *
     * @return 结果
     */
    boolean isEnable();

    /**
     * 设置屏蔽功能开启状态
     *
     * @param enable 开启状态
     */
    void setEnable(boolean enable);

    /**
     * 获取指定梦之工具玩家实例的屏蔽数据实例列表
     *
     * @param player 梦之工具玩家实例
     * @return 屏蔽数据实例列表
     */
    List<IgnoreData> getList(MHDFToolsPlayer player);

    /**
     * 检测指定梦之工具玩家实例是否拥有指定被屏蔽者的梦之工具玩家实例的屏蔽数据实例
     *
     * @param player 梦之工具玩家实例
     * @param ignore 被屏蔽者的梦之工具玩家实例
     * @return 屏蔽数据实例
     */
    boolean hasData(MHDFToolsPlayer player, MHDFToolsPlayer ignore);

    /**
     * 获取指定梦之工具玩家实例的指定被屏蔽者的梦之工具玩家实例的屏蔽数据实例
     *
     * @param player 梦之工具玩家实例
     * @param ignore 被屏蔽者的梦之工具玩家实例
     * @return 屏蔽数据实例
     */
    IgnoreData get(MHDFToolsPlayer player, MHDFToolsPlayer ignore);

    /**
     * 删除指定屏蔽数据实例在数据库中的数据
     *
     * @param ignoreData 屏蔽数据实例
     * @param async      异步处理
     */
    void delete(IgnoreData ignoreData, boolean async);

    /**
     * 删除指定屏蔽数据实例在数据库中的数据
     *
     * @param ignoreData 屏蔽数据实例
     */
    void delete(IgnoreData ignoreData);

    /**
     * 更新指定屏蔽数据实例在数据库中的数据
     *
     * @param ignoreData 屏蔽数据实例
     * @param async      异步处理
     */
    void update(IgnoreData ignoreData, boolean async);

    /**
     * 更新指定屏蔽数据实例在数据库中的数据
     *
     * @param ignoreData 屏蔽数据实例
     */
    void update(IgnoreData ignoreData);
}
