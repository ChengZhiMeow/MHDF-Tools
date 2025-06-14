package cn.chengzhiya.mhdftools.util.database;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.database.NickData;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import lombok.SneakyThrows;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.util.UUID;

public final class NickDataUtil {
    private static final ThreadLocal<Dao<NickData, UUID>> daoThread =
            ThreadLocal.withInitial(() -> {
                try {
                    return DaoManager.createDao(Main.instance.getDatabaseManager().getConnectionSource(), NickData.class);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

    /**
     * 获取dao实例
     *
     * @return dao实例
     */
    public static Dao<NickData, UUID> getDao() {
        return daoThread.get();
    }

    /**
     * 获取指定玩家UUID的匿名数据实例
     *
     * @param uuid 玩家UUID
     * @return 匿名数据实例
     */
    @SneakyThrows
    public static NickData getNickData(UUID uuid) {
        NickData nickData = getDao().queryForId(uuid);
        if (nickData == null) {
            nickData = new NickData();
            nickData.setPlayer(uuid);
        }

        return nickData;
    }

    /**
     * 获取指定玩家实例的匿名数据实例
     *
     * @param player 玩家实例
     * @return 匿名数据实例
     */
    public static NickData getNickData(OfflinePlayer player) {
        return getNickData(player.getUniqueId());
    }

    /**
     * 删除指定玩家UUID的匿名数据实例
     *
     * @param uuid 玩家UUID
     */
    public static void removeNickData(UUID uuid) {
        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            try {
                getDao().deleteById(uuid);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 删除指定玩家实例的匿名数据实例
     *
     * @param player 玩家实例
     */
    public static void removeNickData(OfflinePlayer player) {
        removeNickData(player.getUniqueId());
    }

    /**
     * 更新指定匿名数据实例在数据库中的数据
     *
     * @param nickData 匿名数据实例
     */
    public static void updateNickData(NickData nickData) {
        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            try {
                getDao().createOrUpdate(nickData);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
