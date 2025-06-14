package cn.chengzhiya.mhdftools.util.database;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.database.WarpData;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class WarpDataUtil {
    private static final ThreadLocal<Dao<WarpData, String>> daoThread =
            ThreadLocal.withInitial(() -> {
                try {
                    return DaoManager.createDao(Main.instance.getDatabaseManager().getConnectionSource(), WarpData.class);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

    /**
     * 获取dao实例
     *
     * @return dao实例
     */
    public static Dao<WarpData, String> getDao() {
        return daoThread.get();
    }

    /**
     * 获取指定传送点名称的传送点数据实例
     *
     * @param warp 传送点名称
     * @return 传送点数据实例
     */
    @SneakyThrows
    public static WarpData getWarpData(String warp) {
        return getDao().queryForId(warp);
    }

    /**
     * 获取传送点数据实例列表
     *
     * @return 传送点数据实例列表
     */
    @SneakyThrows
    public static List<WarpData> getHomeDataList() {
        return Objects.requireNonNullElseGet(getDao().queryForAll(), ArrayList::new);
    }

    /**
     * 更新指定传送点数据实例在数据库中的数据
     *
     * @param warpData 传送点数据实例
     */
    public static void updateWarpData(WarpData warpData) {
        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            try {
                getDao().createOrUpdate(warpData);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 删除指定传送点数据实例在数据库中的数据
     *
     * @param warpData 传送点数据实例
     */
    public static void removeWarpData(WarpData warpData) {
        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            try {
                getDao().delete(warpData);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
