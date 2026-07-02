package cn.chengzhimeow.mhdftools.bukkit.api;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.manager.PlayerManager;
import cn.chengzhimeow.mhdftools.api.manager.feature.*;
import cn.chengzhimeow.mhdftools.bukkit.api.database.DatabaseManager;
import cn.chengzhimeow.mhdftools.bukkit.api.manager.PlayerManagerImpl;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import lombok.Getter;

@Getter
public final class MHDFToolsAPIImpl extends MHDFToolsAPI {
    private final PlayerManager playerManager;
    private final EconomyDataManager economyDataManager;
    private final FlyStatusManager flyStatusManager;
    private final HomeDataManager homeDataManager;
    private final IgnoreDataManager ignoreDataManager;
    private final NickDataManager nickDataManager;
    private final PlayerDataManager playerDataManager;
    private final VanishStatusManager vanishStatusManager;
    private final WarpDataManager warpDataManager;
    private final BackDataManager backDataManager;
    private final PvpStatusManager pvpStatusManager;

    public MHDFToolsAPIImpl(DatabaseManager databaseManager) {
        this.playerManager = new PlayerManagerImpl();
        this.economyDataManager = databaseManager.getEconomyDataManager();
        this.flyStatusManager = databaseManager.getFlyStatusManager();
        this.homeDataManager = databaseManager.getHomeDataManager();
        this.ignoreDataManager = databaseManager.getIgnoreDataManager();
        this.nickDataManager = databaseManager.getNickDataManager();
        this.playerDataManager = databaseManager.getPlayerDataManager();
        this.vanishStatusManager = databaseManager.getVanishStatusManager();
        this.warpDataManager = databaseManager.getWarpDataManager();
        this.backDataManager = databaseManager.getBackDataManager();
        this.pvpStatusManager = databaseManager.getPvpStatusManager();
    }

    @Override
    public String getServerName() {
        return BungeeCordManager.getInstance().getServerName();
    }
}
