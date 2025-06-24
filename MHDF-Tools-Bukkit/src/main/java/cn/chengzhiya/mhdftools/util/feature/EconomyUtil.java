package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.entity.database.data.EconomyData;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.database.EconomyDataUtil;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.UUID;

public final class EconomyUtil {
    /**
     * 获取货币名称
     *
     * @return 货币名称
     */
    public static String getMoneyName() {
        return ConfigUtil.getConfig().getString("economySettings.name");
    }

    /**
     * 获取指定玩家UUID的经济实例
     *
     * @param uuid 玩家UUID
     * @return 经济实例
     */
    public static BigDecimal getMoney(UUID uuid) {
        EconomyData economyData = EconomyDataUtil.getEconomyData(uuid);
        if (economyData == null) {
            return BigDecimal.ZERO;
        }
        return economyData.getBigDecimal();
    }

    /**
     * 获取指定玩家实例的经济实例
     *
     * @param player 玩家实例
     * @return 经济实例
     */
    public static BigDecimal getMoney(OfflinePlayer player) {
        return getMoney(player.getUniqueId());
    }

    /**
     * 修改指定玩家UUID的经济实例
     *
     * @param uuid  玩家UUID
     * @param value 修改的值
     */
    public static void setMoney(UUID uuid, BigDecimal value) {
        EconomyData economyData = EconomyDataUtil.getEconomyData(uuid);
        if (economyData == null) {
            economyData = new EconomyData();
            economyData.setPlayer(uuid);
        }

        economyData.setBigDecimal(value);
        EconomyDataUtil.updateEconomyData(economyData);
    }

    /**
     * 修改指定玩家实例的经济实例
     *
     * @param player 玩家实例
     * @param value  修改的值
     */
    public static void setMoney(OfflinePlayer player, BigDecimal value) {
        setMoney(player.getUniqueId(), value);
    }

    /**
     * 增加指定玩家UUID的经济数量实例
     *
     * @param uuid  玩家UUID
     * @param value 增加的值
     */
    public static void addMoney(UUID uuid, BigDecimal value) {
        setMoney(uuid, getMoney(uuid).add(value));
    }

    /**
     * 增加指定玩家实例的经济数量实例
     *
     * @param player 玩家实例
     * @param value  增加的值
     */
    public static void addMoney(OfflinePlayer player, BigDecimal value) {
        addMoney(player.getUniqueId(), value);
    }

    /**
     * 扣除指定玩家UUID的经济数量实例
     *
     * @param uuid  玩家UUID
     * @param value 扣除的值
     */
    public static void takeMoney(UUID uuid, BigDecimal value) {
        setMoney(uuid, getMoney(uuid).subtract(value));
    }

    /**
     * 扣除指定玩家实例的经济数量实例
     *
     * @param player 玩家实例
     * @param value  扣除的值
     */
    public static void takeMoney(OfflinePlayer player, BigDecimal value) {
        takeMoney(player.getUniqueId(), value);
    }
}
