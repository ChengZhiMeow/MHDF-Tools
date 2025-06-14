package cn.chengzhiya.mhdftools.util.database;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.database.VanishStatus;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import lombok.SneakyThrows;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class VanishStatusUtil {
    private static final ThreadLocal<Dao<VanishStatus, UUID>> daoThread =
            ThreadLocal.withInitial(() -> {
                try {
                    return DaoManager.createDao(Main.instance.getDatabaseManager().getConnectionSource(), VanishStatus.class);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

    /**
     * 获取dao实例
     *
     * @return dao实例
     */
    public static Dao<VanishStatus, UUID> getDao() {
        return daoThread.get();
    }

    /**
     * 获取隐身状态实例列表
     *
     * @return 隐身状态实例列表
     */
    @SneakyThrows
    public static List<VanishStatus> getVanishStatusList() {
        return Objects.requireNonNullElseGet(getDao().queryForAll(), ArrayList::new);
    }

    /**
     * 获取指定玩家UUID的隐身状态实例
     *
     * @param uuid 玩家UUID
     * @return 隐身状态实例
     */
    @SneakyThrows
    public static VanishStatus getVanishStatus(UUID uuid) {
        VanishStatus vanishStatus = getDao().queryForId(uuid);
        if (vanishStatus == null) {
            vanishStatus = new VanishStatus();
            vanishStatus.setPlayer(uuid);
        }

        return vanishStatus;
    }

    /**
     * 获取指定玩家实例的隐身状态实例
     *
     * @param player 玩家实例
     * @return 隐身状态实例
     */
    public static VanishStatus getVanishStatus(OfflinePlayer player) {
        return getVanishStatus(player.getUniqueId());
    }

    /**
     * 更新指定隐身状态实例在数据库中的数据
     *
     * @param vanishStatus 隐身状态实例
     */
    public static void updateVanishStatus(VanishStatus vanishStatus) {
        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            try {
                getDao().createOrUpdate(vanishStatus);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
