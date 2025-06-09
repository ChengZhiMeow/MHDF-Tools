package cn.chengzhiya.mhdftools.util.database;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.entity.database.HomeData;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class HomeDataUtil {
    private static final ThreadLocal<Dao<HomeData, Integer>> daoThread =
            ThreadLocal.withInitial(() -> {
                try {
                    return DaoManager.createDao(Main.instance.getDatabaseManager().getConnectionSource(), HomeData.class);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

    /**
     * 获取dao实例
     *
     * @return dao实例
     */
    public static Dao<HomeData, Integer> getDao() {
        return daoThread.get();
    }

    /**
     * 获取指定玩家UUID下指定家名称的家数据实例
     *
     * @param uuid 玩家UUID
     * @param home 家名称
     * @return 家数据实例
     */
    @SneakyThrows
    public static HomeData getHomeData(UUID uuid, String home) {
            return getDao().queryBuilder()
                    .where()
                    .eq("player", uuid)
                    .and()
                    .eq("home", home)
                    .queryForFirst();
    }

    /**
     * 获取指定玩家实例下指定家名称的家数据实例
     *
     * @param player 玩家实例
     * @param home   家名称
     * @return 家数据实例
     */
    public static HomeData getHomeData(Player player, String home) {
        return getHomeData(player.getUniqueId(), home);
    }

    /**
     * 获取指定玩家UUID的家数据实例列表
     *
     * @param uuid 玩家UUID
     * @return 家数据实例列表
     */
    @SneakyThrows
    public static List<HomeData> getHomeDataList(UUID uuid) {
            List<HomeData> homeDataList = getDao().queryBuilder()
                    .where()
                    .eq("player", uuid)
                    .query();

            return Objects.requireNonNullElseGet(homeDataList, ArrayList::new);
    }

    /**
     * 获取指定玩家实例的家数据实例列表
     *
     * @param player 玩家实例
     * @return 家数据实例列表
     */
    public static List<HomeData> getHomeDataList(Player player) {
        return getHomeDataList(player.getUniqueId());
    }

    /**
     * 更新指定家数据实例在数据库中的数据
     *
     * @param homeData 家数据实例
     */
    public static void updateHomeData(HomeData homeData) {
        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            try {
                getDao().createOrUpdate(homeData);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 删除指定家数据实例在数据库中的数据
     *
     * @param homeData 家数据实例
     */
    public static void removeHomeData(HomeData homeData) {
        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            try {
                getDao().delete(homeData);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
