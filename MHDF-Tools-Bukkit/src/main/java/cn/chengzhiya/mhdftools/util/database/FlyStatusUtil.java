package cn.chengzhiya.mhdftools.util.database;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.database.FlyStatus;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import lombok.SneakyThrows;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.util.UUID;

public final class FlyStatusUtil {
    private static final ThreadLocal<Dao<FlyStatus, UUID>> daoThread =
            ThreadLocal.withInitial(() -> {
                try {
                    return DaoManager.createDao(Main.instance.getDatabaseManager().getConnectionSource(), FlyStatus.class);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

    /**
     * 获取dao实例
     *
     * @return dao实例
     */
    public static Dao<FlyStatus, UUID> getDao() {
        return daoThread.get();
    }

    /**
     * 获取指定玩家UUID的飞行状态实例
     *
     * @param uuid 玩家UUID
     * @return 飞行状态实例
     */
    @SneakyThrows
    public static FlyStatus getFlyStatus(UUID uuid) {
        FlyStatus flyStatus = getDao().queryForId(uuid);
        if (flyStatus == null) {
            flyStatus = new FlyStatus();
            flyStatus.setPlayer(uuid);
        }

        return flyStatus;
    }

    /**
     * 获取指定玩家实例的飞行状态实例
     *
     * @param player 玩家实例
     * @return 飞行状态实例
     */
    public static FlyStatus getFlyStatus(OfflinePlayer player) {
        return getFlyStatus(player.getUniqueId());
    }

    /**
     * 更新指定飞行状态实例在数据库中的数据
     *
     * @param flyStatus 飞行状态实例
     */
    public static void updateFlyStatus(FlyStatus flyStatus) {
        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            try {
                getDao().createOrUpdate(flyStatus);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
