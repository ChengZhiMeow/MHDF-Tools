package cn.chengzhiya.mhdftools.util.database;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.database.IgnoreData;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import lombok.SneakyThrows;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class IgnoreDataUtil {
    private static final ThreadLocal<Dao<IgnoreData, Integer>> daoThread =
            ThreadLocal.withInitial(() -> {
                try {
                    return DaoManager.createDao(Main.instance.getDatabaseManager().getConnectionSource(), IgnoreData.class);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

    /**
     * 获取dao实例
     *
     * @return dao实例
     */
    public static Dao<IgnoreData, Integer> getDao() {
        return daoThread.get();
    }

    /**
     * 获取指定玩家UUID的屏蔽数据实例列表
     *
     * @param uuid 玩家UUID
     * @return 屏蔽数据实例列表
     */
    @SneakyThrows
    public static List<IgnoreData> getIgnoreDataList(UUID uuid) {
        List<IgnoreData> ignoreDataList = getDao().queryBuilder()
                .where()
                .eq("player", uuid)
                .query();

        return Objects.requireNonNullElseGet(ignoreDataList, ArrayList::new);
    }

    /**
     * 获取指定玩家实例的屏蔽数据实例列表
     *
     * @param player 玩家实例
     * @return 屏蔽数据实例列表
     */
    public static List<IgnoreData> getIgnoreDataList(OfflinePlayer player) {
        return getIgnoreDataList(player.getUniqueId());
    }

    /**
     * 获取指定玩家UUID屏蔽指定玩家UUID的屏蔽数据实例
     *
     * @param uuid       玩家UUID
     * @param ignoreUuid 被屏蔽的玩家UUID
     * @return 屏蔽数据实例
     */
    @SneakyThrows
    public static IgnoreData getIgnoreData(UUID uuid, UUID ignoreUuid) {
        return getDao().queryBuilder()
                .where()
                .eq("player", uuid)
                .and()
                .eq("ignore", ignoreUuid)
                .queryForFirst();
    }

    /**
     * 获取指定玩家实例屏蔽指定玩家实例的屏蔽数据实例
     *
     * @param player       玩家实例
     * @param ignorePlayer 被屏蔽的玩家实例
     * @return 屏蔽数据实例
     */
    public static IgnoreData getIgnoreData(OfflinePlayer player, OfflinePlayer ignorePlayer) {
        return getIgnoreData(player.getUniqueId(), ignorePlayer.getUniqueId());
    }

    /**
     * 判断指定玩家UUID是否屏蔽指定玩家UUID
     *
     * @param uuid       玩家UUID
     * @param ignoreUuid 被屏蔽的玩家UUID
     * @return 结果
     */
    public static boolean isIgnore(UUID uuid, UUID ignoreUuid) {
        return getIgnoreData(uuid, ignoreUuid) != null;
    }

    /**
     * 判断指定玩家实例是否屏蔽指定玩家实例
     *
     * @param player       玩家实例
     * @param ignorePlayer 被屏蔽的玩家实例
     * @return 结果
     */
    public static boolean isIgnore(OfflinePlayer player, OfflinePlayer ignorePlayer) {
        return isIgnore(player.getUniqueId(), ignorePlayer.getUniqueId());
    }

    /**
     * 更新指定屏蔽数据实例在数据库中的数据
     *
     * @param ignoreData 屏蔽数据实例
     */
    public static void updateIgnoreData(IgnoreData ignoreData) {
        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            try {
                getDao().createOrUpdate(ignoreData);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 移除指定屏蔽数据实例在数据库中的数据
     *
     * @param ignoreData 屏蔽数据实例
     */
    public static void removeIgnoreData(IgnoreData ignoreData) {
        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            try {
                getDao().delete(ignoreData);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
