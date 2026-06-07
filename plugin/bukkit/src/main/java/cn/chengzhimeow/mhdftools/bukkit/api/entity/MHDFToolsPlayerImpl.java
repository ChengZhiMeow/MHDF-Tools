package cn.chengzhimeow.mhdftools.bukkit.api.entity;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.*;
import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.api.message.PlayerMessager;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import lombok.Getter;
import lombok.ToString;
import net.kyori.adventure.text.Component;
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

    public MHDFToolsPlayerImpl(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public MHDFToolsPlayerImpl(OfflinePlayer player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    private void setNickDisplay(TextComponent name) {
        Player player = this.getPlayer();
        if (player == null) {
            return;
        }

        player.displayName(name);
        player.customName(name);
        player.playerListName(name);
        player.setCustomNameVisible(name != null);
    }

    @Override
    public String getName() {
        Player player = this.getPlayer();
        if (player != null) {
            return player.getName();
        }

        if (this.name == null) {
            this.name = MHDFToolsAPI.getInstance().getPlayerDataManager().get(this).getName();
        }
        return this.name;
    }

    @Override
    public String getDisplayName() {
        NickData nickData = this.getNickData();
        if (nickData.getNick() != null) {
            return nickData.getNick();
        }
        return this.getName();
    }

    @Override
    public void sendMessage(Component message) {
        PlayerMessager.send(this, message);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    @Override
    public boolean hasEconomyData() {
        return MHDFToolsAPI.getInstance().getEconomyDataManager().hasData(this);
    }

    @Override
    public EconomyData getEconomyData() {
        return MHDFToolsAPI.getInstance().getEconomyDataManager().get(this);
    }

    @Override
    public BigDecimal getMoney() {
        return this.getEconomyData().getMoney();
    }

    @Override
    public void setMoney(BigDecimal money) {
        EconomyData data = this.getEconomyData();
        data.setMoney(money);
        MHDFToolsAPI.getInstance().getEconomyDataManager().update(data, false);
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
        return player != null && (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR);
    }

    @Override
    public boolean isEnableFly() {
        return MHDFToolsAPI.getInstance().getFlyStatusManager().isEnable(this);
    }

    @Override
    public FlyStatus getFlyStatus() {
        return MHDFToolsAPI.getInstance().getFlyStatusManager().get(this);
    }

    @Override
    public long getFlyTime() {
        return this.getFlyStatus().getTime();
    }

    @Override
    public void setFlyTime(long time) {
        FlyStatus status = this.getFlyStatus();
        status.setTime(time);
        MHDFToolsAPI.getInstance().getFlyStatusManager().update(status);
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
        MHDFToolsAPI.getInstance().getFlyStatusManager().update(status);

        Player player = this.getPlayer();
        if (player != null) {
            player.setAllowFlight(true);
        }
    }

    @Override
    public void disableFly() {
        FlyStatus status = this.getFlyStatus();
        status.setEnable(false);
        MHDFToolsAPI.getInstance().getFlyStatusManager().update(status);

        Player player = this.getPlayer();
        if (player != null) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    @Override
    public List<HomeData> getHomeList() {
        return MHDFToolsAPI.getInstance().getHomeDataManager().getList(this);
    }

    @Override
    public boolean hasHome(String name) {
        return MHDFToolsAPI.getInstance().getHomeDataManager().hasData(this, name);
    }

    @Override
    public HomeData getHome(String name) {
        return MHDFToolsAPI.getInstance().getHomeDataManager().get(this, name);
    }

    @Override
    public void setHome(String name, BungeeCordLocation location) {
        HomeData data = this.getHome(name);
        data.setLocation(location);
        MHDFToolsAPI.getInstance().getHomeDataManager().update(data);
    }

    @Override
    public void deleteHome(String name) {
        MHDFToolsAPI.getInstance().getHomeDataManager().delete(this.getHome(name));
    }

    @Override
    public List<IgnoreData> getIgnoreList() {
        return MHDFToolsAPI.getInstance().getIgnoreDataManager().getList(this);
    }

    @Override
    public boolean isIgnore(MHDFToolsPlayer target) {
        return MHDFToolsAPI.getInstance().getIgnoreDataManager().hasData(this, target);
    }

    @Override
    public IgnoreData getIgnoreData(MHDFToolsPlayer target) {
        return MHDFToolsAPI.getInstance().getIgnoreDataManager().get(this, target);
    }

    @Override
    public void ignore(MHDFToolsPlayer target) {
        if (!this.isIgnore(target)) {
            MHDFToolsAPI.getInstance().getIgnoreDataManager().update(new IgnoreData(this, target));
        }
    }

    @Override
    public void deleteIgnore(MHDFToolsPlayer target) {
        if (this.isIgnore(target)) {
            MHDFToolsAPI.getInstance().getIgnoreDataManager().delete(this.getIgnoreData(target));
        }
    }

    @Override
    public boolean hasNickData() {
        return MHDFToolsAPI.getInstance().getNickDataManager().hasData(this);
    }

    @Override
    public NickData getNickData() {
        return MHDFToolsAPI.getInstance().getNickDataManager().get(this);
    }

    @Override
    public void setNick(String name) {
        NickData data = this.getNickData();
        data.setNick(name);
        MHDFToolsAPI.getInstance().getNickDataManager().update(data);
        this.setNickDisplay(ColorUtil.color(name));
    }

    @Override
    public void deleteNick() {
        MHDFToolsAPI.getInstance().getNickDataManager().delete(this.getNickData());
        this.setNickDisplay(null);
    }

    @Override
    public void showNickDisplay() {
        NickData data = this.getNickData();
        if (data.getNick() != null) {
            this.setNickDisplay(ColorUtil.color(data.getNick()));
        }
    }

    @Override
    public boolean isEnableVanish() {
        return MHDFToolsAPI.getInstance().getVanishStatusManager().isEnable(this);
    }

    @Override
    public VanishStatus getVanishStatus() {
        return MHDFToolsAPI.getInstance().getVanishStatusManager().get(this);
    }

    @Override
    public void enableVanish() {
        VanishStatus status = this.getVanishStatus();
        status.setEnable(true);
        MHDFToolsAPI.getInstance().getVanishStatusManager().update(status);
        this.hidePlayer();
    }

    @Override
    public void hidePlayer() {
        Player player = this.getPlayer();
        if (player == null) {
            return;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(Main.instance, player);
        }
    }

    @Override
    public void disableVanish() {
        VanishStatus status = this.getVanishStatus();
        status.setEnable(false);
        MHDFToolsAPI.getInstance().getVanishStatusManager().update(status);
        this.showPlayer();
    }

    @Override
    public void showPlayer() {
        Player player = this.getPlayer();
        if (player == null) {
            return;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(Main.instance, player);
        }
    }

    @Override
    public List<BackData> getBackDataList(int amount) {
        return MHDFToolsAPI.getInstance().getBackDataManager().getList(this, amount);
    }

    @Override
    public List<BackData> getBackDataList(String type, int amount) {
        return MHDFToolsAPI.getInstance().getBackDataManager().getList(this, type, amount);
    }

    @Override
    public BackData getBackData(int id) {
        return MHDFToolsAPI.getInstance().getBackDataManager().getById(id);
    }

    @Override
    public void addBack(String type, BungeeCordLocation location) {
        MHDFToolsAPI.getInstance().getBackDataManager().update(new BackData(this, type, location));
    }

    @Override
    public boolean isEnablePvp() {
        return MHDFToolsAPI.getInstance().getPvpStatusManager().isEnable(this);
    }

    @Override
    public PvpStatus getPvpStatus() {
        return MHDFToolsAPI.getInstance().getPvpStatusManager().get(this);
    }

    @Override
    public void enablePvp() {
        PvpStatus status = this.getPvpStatus();
        status.setEnable(true);
        MHDFToolsAPI.getInstance().getPvpStatusManager().update(status);
    }

    @Override
    public void disablePvp() {
        PvpStatus status = this.getPvpStatus();
        status.setEnable(false);
        MHDFToolsAPI.getInstance().getPvpStatusManager().update(status);
    }

    @Override
    public boolean equals(MHDFToolsPlayer target) {
        return this.uuid.equals(target.getUuid());
    }
}
