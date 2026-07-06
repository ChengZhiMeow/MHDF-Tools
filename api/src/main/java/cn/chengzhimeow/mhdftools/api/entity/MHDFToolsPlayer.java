package cn.chengzhimeow.mhdftools.api.entity;

import cn.chengzhimeow.mhdftools.api.entity.database.data.*;
import cn.chengzhimeow.mhdftools.api.entity.location.BukkitLocation;
import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface MHDFToolsPlayer {
    UUID getUuid();

    String getName();

    @Nullable String getNameOrNull();

    /**
     * 获取显示名称
     *
     * @return 显示名称
     */
    String getDisplayName();

    /**
     * 发送消息给玩家
     *
     * @param message 消息
     */
    void sendMessage(Component message);

    /**
     * 传送到指定梦之工具玩家实例
     *
     * @param target 梦之工具玩家实例
     */
    void teleport(MHDFToolsPlayer target);

    /**
     * 传送到指定群组位置实例
     *
     * @param location 群组位置实例
     */
    void teleport(BungeeCordLocation location);

    /**
     * 传送到指定Bukkit位置实例
     *
     * @param location Bukkit位置实例
     */
    void teleport(BukkitLocation location);

    /**
     * 检测是否拥有经济数据
     *
     * @return 结果
     */
    boolean hasEconomyData();

    /**
     * 获取经济数据实例
     *
     * @return 经济数据实例
     */
    EconomyData getEconomyData();

    /**
     * 获取经济数量
     *
     * @return 经济数量
     */
    BigDecimal getMoney();

    /**
     * 修改经济数量
     *
     * @param money 经济数量
     */
    void setMoney(BigDecimal money);

    /**
     * 增加经济数量
     *
     * @param money 经济数量
     */
    void addMoney(BigDecimal money);

    /**
     * 扣除经济数量
     *
     * @param money 经济数量
     */
    void takeMoney(BigDecimal money);

    /**
     * 检测当前游戏模式是否可以飞行
     */
    boolean isAllowedFlyingGameMode();

    /**
     * 检测是否启用飞行
     *
     * @return 结果
     */
    boolean isEnableFly();

    /**
     * 获取飞行状态实例
     *
     * @return 飞行状态实例
     */
    FlyStatus getFlyStatus();

    /**
     * 获取限时飞行时间
     */
    long getFlyTime();

    /**
     * 修改限时飞行时间
     *
     * @param time 时间
     */
    void setFlyTime(long time);

    /**
     * 增加限时飞行时间
     *
     * @param time 时间
     */
    void addFlyTime(long time);

    /**
     * 扣除限时飞行时间
     *
     * @param time 时间
     */
    void takeFlyTime(long time);

    /**
     * 打开飞行
     */
    void enableFly();

    /**
     * 关闭飞行
     */
    void disableFly();

    /**
     * 获取家数据实例列表
     *
     * @return 家数据实例列表
     */
    List<HomeData> getHomeList();

    /**
     * 检测是否拥有指定名称的家
     *
     * @param name 家名称
     * @return 结果
     */
    boolean hasHome(String name);

    /**
     * 获取指定名称的家数据实例
     *
     * @param name 家名称
     * @return 家数据实例
     */
    HomeData getHome(String name);

    /**
     * 设置指定名称的家数据实例
     *
     * @param name     家名称
     * @param location 群组位置实例
     */
    void setHome(String name, BungeeCordLocation location);

    /**
     * 删除指定名称的家
     *
     * @param name 家名称
     */
    void deleteHome(String name);

    /**
     * 获取忽略数据实例列表
     *
     * @return 忽略数据实例列表
     */
    List<IgnoreData> getIgnoreList();

    /**
     * 检测是否忽略指定梦之工具玩家实例
     *
     * @param target 梦之工具玩家实例
     * @return 结果
     */
    boolean isIgnore(MHDFToolsPlayer target);

    /**
     * 虎丘指定梦之工具玩家实例的忽略数据实例
     *
     * @param target 梦之工具玩家实例
     * @return 忽略数据实例cc
     */
    IgnoreData getIgnoreData(MHDFToolsPlayer target);

    /**
     * 忽略指定梦之工具玩家实例
     *
     * @param target 梦之工具玩家实例
     */
    void ignore(MHDFToolsPlayer target);

    /**
     * 解除忽略指定梦之工具玩家实例
     *
     * @param target 梦之工具玩家实例
     */
    void deleteIgnore(MHDFToolsPlayer target);

    /**
     * 检测是否拥有匿名数据
     *
     * @return 结果
     */
    boolean hasNickData();

    /**
     * 获取匿名数据实例
     *
     * @return 匿名数据实例
     */
    NickData getNickData();

    /**
     * 设置匿名名称
     *
     * @param name 名称
     */
    void setNick(String name);

    /**
     * 删除匿名名称
     */
    void deleteNick();

    /**
     * 显示匿名名称
     */
    void showNickDisplay();

    /**
     * 检测是否开启隐身
     *
     * @return 结果
     */
    boolean isEnableVanish();

    /**
     * 获取隐身状态实例
     *
     * @return 隐身状态实例
     */
    VanishStatus getVanishStatus();

    /**
     * 开启隐身
     */
    void enableVanish();

    /**
     * 隐藏玩家
     */
    void hidePlayer();

    /**
     * 关闭隐身
     */
    void disableVanish();

    /**
     * 显示玩家
     */
    void showPlayer();

    /**
     * 获取位置记录数据实例列表
     *
     * @param amount 数量
     * @return 位置记录数据实例列表
     */
    List<BackData> getBackDataList(int amount);

    /**
     * 获取位置记录数据实例列表
     *
     * @param type   位置类型
     * @param amount 数量
     * @return 位置记录数据实例列表
     */
    List<BackData> getBackDataList(String type, int amount);

    /**
     * 获取位置记录数据实例
     *
     * @param id 位置记录数据ID
     * @return 位置记录数据实例
     */
    BackData getBackData(int id);

    /**
     * 添加位置记录
     *
     * @param type     位置类型
     * @param location 群组位置实例
     */
    void addBack(String type, BungeeCordLocation location);

    /**
     * 检测是否开启PVP
     *
     * @return 结果
     */
    boolean isEnablePvp();

    /**
     * 获取PVP状态实例
     *
     * @return PVP状态实例
     */
    PvpStatus getPvpStatus();

    /**
     * 开启PVP
     */
    void enablePvp();

    /**
     * 关闭PVP
     */
    void disablePvp();

    boolean equals(MHDFToolsPlayer target);

    String toString();
}
