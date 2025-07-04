package cn.chengzhiya.mhdftools.api;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.api.manager.PlayerManager;
import cn.chengzhiya.mhdftools.api.manager.PlayerManagerImpl;
import cn.chengzhiya.mhdftools.api.manager.feature.*;
import lombok.Getter;

@Getter
public final class MHDFToolsAPIImpl implements MHDFToolsAPI {
    private final PlayerManager playerManager;

    private final EconomyDataManager economyDataManager;
    private final FlyStatusManager flyStatusManager;
    private final HomeDataManager homeDataManager;
    private final IgnoreDataManager ignoreDataManager;
    private final NickDataManager nickDataManager;
    private final PlayerDataManager playerDataManager;
    private final VanishStatusManager vanishStatusManager;
    private final WarpDataManager warpDataManager;

    public MHDFToolsAPIImpl() {
        this.playerManager = new PlayerManagerImpl();

        this.economyDataManager = new EconomyDataManagerImpl();
        this.flyStatusManager = new FlyStatusManagerImpl();
        this.homeDataManager = new HomeDataManagerImpl();
        this.ignoreDataManager = new IgnoreDataManagerImpl();
        this.nickDataManager = new NickDataManagerImpl();
        this.playerDataManager = new PlayerDataManagerImpl();
        this.vanishStatusManager = new VanishStatusManagerImpl();
        this.warpDataManager = new WarpDataManagerImpl();
    }

    @Override
    public String getServerName() {
        return Main.instance.getBungeeCordManager().getServerName();
    }
}
