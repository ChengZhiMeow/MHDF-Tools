package cn.chengzhiya.mhdftools.api.manager.feature;

import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.data.HomeData;

import java.util.List;

public interface HomeDataManager {
    /**
     * 获取指定梦之工具玩家实例的家数据实例列表
     *
     * @return 家数据实例列表
     */
    List<HomeData> getList(MHDFToolsPlayer player);

    /**
     * 检测指定梦之工具玩家实例是否拥有指定名称的家
     *
     * @param name 传动点名称
     * @return 结果
     */
    boolean hasData(MHDFToolsPlayer player, String name);

    /**
     * 获取指定梦之工具玩家实例下指定名称的家数据实例
     *
     * @param name 传送点名称
     * @return 家数据实例
     */
    HomeData get(MHDFToolsPlayer player, String name);

    /**
     * 删除指定家数据实例在数据库中的数据
     *
     * @param homeData 家数据实例
     */
    void delete(HomeData homeData);

    /**
     * 更新指定家数据实例在数据库中的数据
     *
     * @param homeData 家数据实例
     */
    void update(HomeData homeData);
}
