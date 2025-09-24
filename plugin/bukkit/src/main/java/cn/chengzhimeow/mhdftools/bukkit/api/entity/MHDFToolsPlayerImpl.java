package cn.chengzhimeow.mhdftools.bukkit.api.entity;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.*;
import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.text.TextComponent;
import cn.chengzhimeow.mhdftools.bukkit.util.message.ColorUtil;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public final class MHDFToolsPlayerImpl implements MHDFToolsPlayer {
    private final UUID uuid;
    private String name;

    public MHDFToolsPlayerImpl(UUID uuid) {
        this.uuid = uuid;
    }

    public MHDFToolsPlayerImpl(OfflinePlayer player) {
        this.uuid = player.getUniqueId();
    }

    /**
     * 设置指定玩家实例的匿名昵称显示
     *
     * @param name 匿名昵称
     */
    private void setNickDisplay(TextComponent name) {
        if (this.getPlayer() == null) {
            return;
        }

        this.getPlayer().displayName(name);
        this.getPlayer().customName(name);
        this.getPlayer().playerListName(name);

        this.getPlayer().setCustomNameVisible(name != null);
    }

    @Override
    public String getName() {
        if (this.getPlayer() != null) {
            return this.getPlayer().getName();
        }

        if (this.name == null) {
            PlayerData data = MHDFToolsAPIHelper.getInstance().getPlayerDataManager().get(this);
            this.name = data.getName();
        }

        return this.name;
    }

    @Override
    public String getDisplayName() {
        if (ConfigSetting.getSettingInstance().getData().getBoolean("nickSettings.enable") && this.hasNickData()) {
            return this.getNickData().getNick();
        }

        return this.getName();
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    @Override
    public boolean hasEconomyData() {
        return MHDFToolsAPIHelper.getInstance().getEconomyDataManager().hasData(this);
    }

    @Override
    public EconomyData getEconomyData() {
        return MHDFToolsAPIHelper.getInstance().getEconomyDataManager().get(this);
    }

    @Override
    public BigDecimal getMoney() {
        return this.getEconomyData().getMoney();
    }

    @Override
    public void setMoney(BigDecimal money) {
        EconomyData data = this.getEconomyData();
        data.setMoney(money);
        MHDFToolsAPIHelper.getInstance().getEconomyDataManager().update(data, false);
    }

    @Override
    public void addMoney(BigDecimal money) {
        this.setMoney(this.getMoney().add(money));
    }

    @Override
    public void takeMoney(BigDecimal money) {
        this.setMoney(this.getMoney().subtract(money));
    }

    @Override
    public boolean isAllowedFlyingGameMode() {
        Player player = this.getPlayer();
        if (player == null) {
            return false;
        }

        return player.getGameMode() == GameMode.CREATIVE ||
                player.getGameMode() == GameMode.SPECTATOR;
    }

    @Override
    public boolean isEnableFly() {
        return MHDFToolsAPIHelper.getInstance().getFlyStatusManager().isEnable(this);
    }

    @Override
    public FlyStatus getFlyStatus() {
        return MHDFToolsAPIHelper.getInstance().getFlyStatusManager().get(this);
    }

    @Override
    public long getFlyTime() {
        return this.getFlyStatus().getTime();
    }

    @Override
    public void setFlyTime(long time) {
        FlyStatus status = this.getFlyStatus();
        status.setTime(time);

        MHDFToolsAPIHelper.getInstance().getFlyStatusManager().update(status);
    }

    @Override
    public void addFlyTime(long time) {
        this.setFlyTime(this.getFlyTime() + time);
    }

    @Override
    public void takeFlyTime(long time) {
        this.setFlyTime(this.getFlyTime() - time);
    }

    @Override
    public void enableFly() {
        FlyStatus status = this.getFlyStatus();
        status.setEnable(true);
        MHDFToolsAPIHelper.getInstance().getFlyStatusManager().update(status);

        if (this.getPlayer() != null) {
            this.getPlayer().setAllowFlight(true);
        }
    }

    @Override
    public void disableFly() {
        FlyStatus status = this.getFlyStatus();
        status.setEnable(false);
        MHDFToolsAPIHelper.getInstance().getFlyStatusManager().update(status);

        if (this.getPlayer() != null) {
            this.getPlayer().setAllowFlight(false);
        }
    }

    @Override
    public List<HomeData> getHomeList() {
        return MHDFToolsAPIHelper.getInstance().getHomeDataManager().getList(this);
    }

    @Override
    public boolean hasHome(String name) {
        return MHDFToolsAPIHelper.getInstance().getHomeDataManager().hasData(this, name);
    }

    @Override
    public HomeData getHome(String name) {
        return MHDFToolsAPIHelper.getInstance().getHomeDataManager().get(this, name);
    }

    @Override
    public void setHome(String name, BungeeCordLocation location) {
        HomeData data = MHDFToolsAPIHelper.getInstance().getHomeDataManager().get(this, name);
        data.setLocation(location);
        MHDFToolsAPIHelper.getInstance().getHomeDataManager().update(data);
    }

    @Override
    public void deleteHome(String name) {
        HomeData data = MHDFToolsAPIHelper.getInstance().getHomeDataManager().get(this, name);
        MHDFToolsAPIHelper.getInstance().getHomeDataManager().delete(data);
    }

    @Override
    public List<IgnoreData> getIgnoreList() {
        return MHDFToolsAPIHelper.getInstance().getIgnoreDataManager().getList(this);
    }

    @Override
    public boolean isIgnore(MHDFToolsPlayer target) {
        return MHDFToolsAPIHelper.getInstance().getIgnoreDataManager().hasData(this, target);
    }

    @Override
    public IgnoreData getIgnoreData(MHDFToolsPlayer target) {
        return MHDFToolsAPIHelper.getInstance().getIgnoreDataManager().get(this, target);
    }

    @Override
    public void ignore(MHDFToolsPlayer target) {
        if (this.isIgnore(target)) {
            return;
        }

        IgnoreData data = new IgnoreData(this, target);
        MHDFToolsAPIHelper.getInstance().getIgnoreDataManager().update(data);
    }

    @Override
    public void deleteIgnore(MHDFToolsPlayer target) {
        if (!this.isIgnore(target)) {
            return;
        }

        IgnoreData data = this.getIgnoreData(target);
        MHDFToolsAPIHelper.getInstance().getIgnoreDataManager().delete(data);
    }

    @Override
    public boolean hasNickData() {
        return MHDFToolsAPIHelper.getInstance().getNickDataManager().hasData(this);
    }

    @Override
    public NickData getNickData() {
        return MHDFToolsAPIHelper.getInstance().getNickDataManager().get(this);
    }

    @Override
    public void setNick(String name) {
        NickData data = this.getNickData();
        data.setNick(name);
        MHDFToolsAPIHelper.getInstance().getNickDataManager().update(data);

        this.setNickDisplay(ColorUtil.color(name));
    }

    @Override
    public void deleteNick() {
        NickData data = this.getNickData();
        MHDFToolsAPIHelper.getInstance().getNickDataManager().delete(data);

        this.setNickDisplay(null);
    }

    @Override
    public void showNickDisplay() {
        NickData data = this.getNickData();
        this.setNickDisplay(ColorUtil.color(data.getNick()));
    }

    @Override
    public boolean isEnableVanish() {
        return MHDFToolsAPIHelper.getInstance().getVanishStatusManager().isEnable(this);
    }

    @Override
    public VanishStatus getVanishStatus() {
        return MHDFToolsAPIHelper.getInstance().getVanishStatusManager().get(this);
    }

    @Override
    public void enableVanish() {
        VanishStatus status = this.getVanishStatus();
        status.setEnable(true);
        MHDFToolsAPIHelper.getInstance().getVanishStatusManager().update(status);

        this.hidePlayer();
    }

    @Override
    public void hidePlayer() {
        if (this.getPlayer() == null) return;

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (Main.instance.getPluginHookManager().getPacketEventsHook().getServerVersion()
                    .isNewerThanOrEquals(ServerVersion.V_1_12_2)
            ) {
                CCScheduler.getInstance().getGlobalRegionScheduler().runTask(Main.instance, () -> {
                    onlinePlayer.hidePlayer(Main.instance, this.getPlayer());
                });
                continue;
            }

            CCScheduler.getInstance().getGlobalRegionScheduler().runTask(Main.instance, () ->
                    onlinePlayer.hidePlayer(this.getPlayer()));
        }
    }

    @Override
    public void disableVanish() {
        VanishStatus status = this.getVanishStatus();
        status.setEnable(false);
        MHDFToolsAPIHelper.getInstance().getVanishStatusManager().update(status);

        this.showPlayer();
    }

    @Override
    public void showPlayer() {
        if (this.getPlayer() == null) return;

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (Main.instance.getPluginHookManager().getPacketEventsHook().getServerVersion()
                    .isNewerThanOrEquals(ServerVersion.V_1_12_2)
            ) {
                CCScheduler.getInstance().getGlobalRegionScheduler().runTask(Main.instance, () ->
                        onlinePlayer.showPlayer(Main.instance, this.getPlayer()));
                continue;
            }

            CCScheduler.getInstance().getGlobalRegionScheduler().runTask(Main.instance, () ->
                    onlinePlayer.showPlayer(this.getPlayer()));
        }
    }

    @Override
    public List<BackData> getBackDataList(int amount) {
        return MHDFToolsAPIHelper.getInstance().getBackDataManager().getList(this, amount);
    }

    @Override
    public List<BackData> getBackDataList(String type, int amount) {
        return MHDFToolsAPIHelper.getInstance().getBackDataManager().getList(this, type, amount);
    }

    @Override
    public BackData getBackData(int id) {
        return MHDFToolsAPIHelper.getInstance().getBackDataManager().getById(id);
    }

    @Override
    public void addBack(String type, BungeeCordLocation location) {
        MHDFToolsAPIHelper.getInstance().getBackDataManager().update(new BackData(this, type, location));
    }

    @Override
    public boolean isEnablePvp() {
        return MHDFToolsAPIHelper.getInstance().getPvpStatusManager().isEnable(this);
    }

    @Override
    public PvpStatus getPvpStatus() {
        return MHDFToolsAPIHelper.getInstance().getPvpStatusManager().get(this);
    }

    @Override
    public void enablePvp() {
        PvpStatus status = this.getPvpStatus();
        status.setEnable(true);
        MHDFToolsAPIHelper.getInstance().getPvpStatusManager().update(status);
    }

    @Override
    public void disablePvp() {
        PvpStatus status = this.getPvpStatus();
        status.setEnable(false);
        MHDFToolsAPIHelper.getInstance().getPvpStatusManager().update(status);
    }

    @Override
    public boolean equals(MHDFToolsPlayer target) {
        return this.getUuid().equals(target.getUuid());
    }
}
