package cn.chengzhimeow.mhdftools.bukkit.api.database;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.api.entity.database.data.*;
import cn.chengzhimeow.mhdftools.bukkit.api.manager.feature.*;
import cn.chengzhimeow.mhdftools.bukkit.common.config.DatabaseSetting;
import cn.chengzhiya.mhdfdatabase.MHDFDatabase;
import cn.chengzhiya.mhdfdatabase.entity.DatabaseConfig;
import cn.chengzhiya.mhdfdatabase.entity.DatabaseConnectConfig;
import cn.chengzhiya.mhdfdatabase.impl.H2DatabaseServiceImpl;
import cn.chengzhiya.mhdfdatabase.impl.MySQLDatabaseServiceImpl;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class DatabaseManager implements AutoCloseable {
    private final DatabaseConfig config;
    private final DatabaseCache cache;
    private final MHDFDatabase database;
    private final String moneyName;
    private final double defaultMoney;

    private final PlayerDataManagerImpl playerDataManager;
    private final EconomyDataManagerImpl economyDataManager;
    private final FlyStatusManagerImpl flyStatusManager;
    private final PvpStatusManagerImpl pvpStatusManager;
    private final VanishStatusManagerImpl vanishStatusManager;
    private final NickDataManagerImpl nickDataManager;
    private final HomeDataManagerImpl homeDataManager;
    private final IgnoreDataManagerImpl ignoreDataManager;
    private final WarpDataManagerImpl warpDataManager;
    private final BackDataManagerImpl backDataManager;

    @SneakyThrows
    public DatabaseManager(JavaPlugin plugin, ConfigurationSection root) {
        this.config = this.databaseConfig(plugin);
        this.cache = new DatabaseCache();
        this.database = new MHDFDatabase(this.config, MySQLDatabaseServiceImpl.class, H2DatabaseServiceImpl.class);
        this.moneyName = root == null ? "金币" : root.getString("economySettings.name", "金币");
        this.defaultMoney = root == null ? 0D : root.getDouble("economySettings.default");

        this.addTables();
        this.playerDataManager = new PlayerDataManagerImpl(this);
        this.economyDataManager = new EconomyDataManagerImpl(this);
        this.flyStatusManager = new FlyStatusManagerImpl(this);
        this.pvpStatusManager = new PvpStatusManagerImpl(this);
        this.vanishStatusManager = new VanishStatusManagerImpl(this);
        this.nickDataManager = new NickDataManagerImpl(this);
        this.homeDataManager = new HomeDataManagerImpl(this);
        this.ignoreDataManager = new IgnoreDataManagerImpl(this);
        this.warpDataManager = new WarpDataManagerImpl(this);
        this.backDataManager = new BackDataManagerImpl(this);
    }

    private DatabaseConfig databaseConfig(JavaPlugin plugin) {
        DatabaseSetting.Config setting = DatabaseSetting.getInstance().getConfig();
        DatabaseConnectConfig connectConfig = new DatabaseConnectConfig();
        connectConfig.setHost(setting.mysql().host());
        connectConfig.setDatabase(setting.mysql().database());
        connectConfig.setUser(setting.mysql().user());
        connectConfig.setPassword(setting.mysql().password());
        connectConfig.setFile(new File(plugin.getDataFolder(), setting.h2().file()));
        connectConfig.getPramHashMap().putAll(setting.prams());

        DatabaseConfig config = new DatabaseConfig();
        config.setType(setting.type());
        config.setConnectConfig(connectConfig);
        return config;
    }

    private void addTables() {
        this.database.addTable(PlayerData.class);
        this.database.addTable(EconomyData.class);
        this.database.addTable(FlyStatus.class);
        this.database.addTable(PvpStatus.class);
        this.database.addTable(VanishStatus.class);
        this.database.addTable(NickData.class);
        this.database.addTable(HomeData.class);
        this.database.addTable(IgnoreData.class);
        this.database.addTable(WarpData.class);
        this.database.addTable(BackData.class);
    }

    public void connect() {
        this.database.getDatabaseService().connect();
    }

    public void initTable() {
        this.database.createAllTable();
    }

    @Override
    public void close() {
        this.database.getDatabaseService().close();
    }
}
