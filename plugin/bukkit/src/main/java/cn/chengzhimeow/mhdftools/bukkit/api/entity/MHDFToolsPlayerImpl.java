package cn.chengzhimeow.mhdftools.bukkit.api.entity;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.*;
import cn.chengzhimeow.mhdftools.api.entity.location.BukkitLocation;
import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import cn.chengzhimeow.mhdftools.api.manager.feature.*;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkitAdapt;
import cn.chengzhimeow.mhdftools.bukkit.common.bungee.BungeeCordManager;
import cn.chengzhimeow.mhdftools.bukkit.common.message.Messager;
import cn.chengzhimeow.mhdftools.bukkit.module.core.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.core.listener.ServerTeleport;
import cn.chengzhimeow.mhdftools.bukkit.module.core.message.PlayerMessage;
import cn.chengzhimeow.mhdftools.bukkit.module.core.message.PlayerTeleportMessage;
import cn.chengzhimeow.mhdftools.config.impl.GlobalLangSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import lombok.Getter;
import lombok.ToString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.nyana.nbt.NBT;
import net.nyana.nbt.tag.CompoundTag;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
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
        if (player == null) return;

        player.displayName(name);
        player.customName(name);
        player.playerListName(name);
        player.setCustomNameVisible(name != null);
    }

    @Override
    public String getName() {
        String name = this.getNameOrNull();

        return this.name;
    }

    @Override
    public @Nullable String getNameOrNull() {
        if (this.name != null) return this.name;

        Player player = this.getPlayer();
        if (player != null)
            this.name = player.getName();

        return this.name;
    }

    @Override
    public String getDisplayName() {
        NickDataManager manager = MHDFToolsAPI.getInstance().getNickDataManager();
        if (!manager.isEnable()) return this.getName();

        NickData nickData = manager.get(this);
        if (nickData.getNick() == null) return this.getName();
        return nickData.getNick();
    }

    @Override
    public void sendMessage(Component message) {
        Player player = this.getPlayer();
        if (player == null) player = Bukkit.getPlayerExact(this.getName());
        if (player != null) {
            player.sendMessage(message);
            return;
        }

        BungeeCordManager bungeeCordManager = BungeeCordManager.getInstance();
        if (bungeeCordManager == null || !bungeeCordManager.isBungeeCordMode()) return;
        if (!bungeeCordManager.ifPlayerOnline(this.getName())) return;
        if (!Main.instance.getRedisManager().isOpen()) return;

        Messager.publish(new PlayerMessage(
                bungeeCordManager.getServerName(),
                this.getName(),
                GsonComponentSerializer.gson().serialize(message)
        ));
    }

    @Override
    public void teleport(MHDFToolsPlayer target) {
        Player player = this.getPlayer();
        if (player == null) return;

        Player targetPlayer = Bukkit.getPlayer(target.getUuid());
        if (targetPlayer == null) targetPlayer = Bukkit.getPlayerExact(target.getName());
        if (targetPlayer != null && targetPlayer.isOnline()) {
            this.teleport(MHDFToolsBukkitAdapt.adapt(targetPlayer.getLocation()));
            return;
        }

        BungeeCordManager bungeeCordManager = BungeeCordManager.getInstance();
        if (!bungeeCordManager.isBungeeCordMode() || !bungeeCordManager.getPlayerList().contains(target.getName())) {
            player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
            return;
        }

        if (!Main.instance.getRedisManager().isOpen()) {
            player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
            return;
        }

        try {
            CompoundTag root = NBT.createCompound();
            CompoundTag info = NBT.createCompound();
            root.putByte("mode", (byte) 0);
            info.putString("player", target.getName());
            root.put("info", info);

            ModuleMain.instance.serverTeleportCache.put(this.getName(), NBT.toBytes(root));
        } catch (IOException ignored) {
            player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
            return;
        }

        Messager.publish(new PlayerTeleportMessage(
                bungeeCordManager.getServerName(),
                this.getName(),
                target.getName()
        ));
    }

    @Override
    public void teleport(BungeeCordLocation location) {
        Player player = this.getPlayer();
        if (player == null) return;

        BungeeCordManager bungeeCordManager = BungeeCordManager.getInstance();
        if (bungeeCordManager.isBungeeCordMode()) {
            if (!location.getServer().equalsIgnoreCase(bungeeCordManager.getServerName())) {
                try {
                    CompoundTag root = NBT.createCompound();
                    CompoundTag info = NBT.createCompound();
                    root.putByte("mode", (byte) 1);
                    info.putString("server_id", location.getServer());
                    info.putString("world", location.getWorld());
                    info.putDouble("x", location.getX());
                    info.putDouble("y", location.getY());
                    info.putDouble("z", location.getZ());
                    info.putFloat("yaw", location.getYaw());
                    info.putFloat("pitch", location.getPitch());
                    root.put("info", info);

                    ModuleMain.instance.serverTeleportCache.put(this.getName(), NBT.toBytes(root));
                } catch (IOException ignored) {
                    player.sendMessage(GlobalLangSetting.getInstance().getConfig().serverTeleportFailed());
                    return;
                }

                bungeeCordManager.connectServer(this.getName(), location.getServer());
                return;
            }
        }

        this.teleport(location.getLocation());
    }

    @Override
    public void teleport(BukkitLocation location) {
        Player player = this.getPlayer();
        if (player == null) return;

        ServerTeleport.teleport(player, location);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    @Override
    public boolean hasEconomyData() {
        EconomyDataManager manager = MHDFToolsAPI.getInstance().getEconomyDataManager();
        if (!manager.isEnable()) return false;
        return manager.hasData(this);
    }

    @Override
    public EconomyData getEconomyData() {
        EconomyDataManager manager = MHDFToolsAPI.getInstance().getEconomyDataManager();
        if (!manager.isEnable()) throw new IllegalStateException("经济功能未开启");
        return manager.get(this);
    }

    @Override
    public BigDecimal getMoney() {
        return this.getEconomyData().getMoney();
    }

    @Override
    public void setMoney(BigDecimal money) {
        EconomyDataManager manager = MHDFToolsAPI.getInstance().getEconomyDataManager();
        if (!manager.isEnable()) throw new IllegalStateException("经济功能未开启");

        EconomyData data = this.getEconomyData();
        data.setMoney(money);
        manager.update(data, false);
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
        FlyStatusManager manager = MHDFToolsAPI.getInstance().getFlyStatusManager();
        return manager.isEnable() && manager.isEnable(this);
    }

    @Override
    public FlyStatus getFlyStatus() {
        FlyStatusManager manager = MHDFToolsAPI.getInstance().getFlyStatusManager();
        if (!manager.isEnable()) throw new IllegalStateException("飞行功能未开启");
        return manager.get(this);
    }

    @Override
    public long getFlyTime() {
        return this.getFlyStatus().getTime();
    }

    @Override
    public void setFlyTime(long time) {
        FlyStatusManager manager = MHDFToolsAPI.getInstance().getFlyStatusManager();
        if (!manager.isEnable()) throw new IllegalStateException("飞行功能未开启");

        FlyStatus status = this.getFlyStatus();
        status.setTime(time);
        manager.update(status);
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
        FlyStatusManager manager = MHDFToolsAPI.getInstance().getFlyStatusManager();
        if (!manager.isEnable()) throw new IllegalStateException("飞行功能未开启");

        FlyStatus status = this.getFlyStatus();
        status.setEnable(true);
        manager.update(status);

        Player player = this.getPlayer();
        if (player == null) return;
        player.setAllowFlight(true);
    }

    @Override
    public void disableFly() {
        FlyStatusManager manager = MHDFToolsAPI.getInstance().getFlyStatusManager();
        if (!manager.isEnable()) throw new IllegalStateException("飞行功能未开启");

        FlyStatus status = this.getFlyStatus();
        status.setEnable(false);
        manager.update(status);

        Player player = this.getPlayer();
        if (player == null) return;
        player.setFlying(false);
        player.setAllowFlight(false);
    }

    @Override
    public List<HomeData> getHomeList() {
        HomeDataManager manager = MHDFToolsAPI.getInstance().getHomeDataManager();
        if (!manager.isEnable()) return List.of();
        return manager.getList(this);
    }

    @Override
    public boolean hasHome(String name) {
        HomeDataManager manager = MHDFToolsAPI.getInstance().getHomeDataManager();
        return manager.isEnable() && manager.hasData(this, name);
    }

    @Override
    public HomeData getHome(String name) {
        HomeDataManager manager = MHDFToolsAPI.getInstance().getHomeDataManager();
        if (!manager.isEnable()) throw new IllegalStateException("家功能未开启");
        return manager.get(this, name);
    }

    @Override
    public void setHome(String name, BungeeCordLocation location) {
        HomeDataManager manager = MHDFToolsAPI.getInstance().getHomeDataManager();
        if (!manager.isEnable()) throw new IllegalStateException("家功能未开启");

        HomeData data = this.getHome(name);
        data.setLocation(location);
        manager.update(data);
    }

    @Override
    public void deleteHome(String name) {
        HomeDataManager manager = MHDFToolsAPI.getInstance().getHomeDataManager();
        if (!manager.isEnable()) throw new IllegalStateException("家功能未开启");
        manager.delete(this.getHome(name));
    }

    @Override
    public List<IgnoreData> getIgnoreList() {
        IgnoreDataManager manager = MHDFToolsAPI.getInstance().getIgnoreDataManager();
        if (!manager.isEnable()) return List.of();
        return manager.getList(this);
    }

    @Override
    public boolean isIgnore(MHDFToolsPlayer target) {
        IgnoreDataManager manager = MHDFToolsAPI.getInstance().getIgnoreDataManager();
        return manager.isEnable() && manager.hasData(this, target);
    }

    @Override
    public IgnoreData getIgnoreData(MHDFToolsPlayer target) {
        IgnoreDataManager manager = MHDFToolsAPI.getInstance().getIgnoreDataManager();
        if (!manager.isEnable()) throw new IllegalStateException("屏蔽功能未开启");
        return manager.get(this, target);
    }

    @Override
    public void ignore(MHDFToolsPlayer target) {
        IgnoreDataManager manager = MHDFToolsAPI.getInstance().getIgnoreDataManager();
        if (!manager.isEnable()) throw new IllegalStateException("屏蔽功能未开启");

        if (this.isIgnore(target)) return;
        manager.update(new IgnoreData(this, target));
    }

    @Override
    public void deleteIgnore(MHDFToolsPlayer target) {
        IgnoreDataManager manager = MHDFToolsAPI.getInstance().getIgnoreDataManager();
        if (!manager.isEnable()) throw new IllegalStateException("屏蔽功能未开启");

        if (this.isIgnore(target)) {
            manager.delete(this.getIgnoreData(target));
        }
    }

    @Override
    public boolean hasNickData() {
        NickDataManager manager = MHDFToolsAPI.getInstance().getNickDataManager();
        return manager.isEnable() && manager.hasData(this);
    }

    @Override
    public NickData getNickData() {
        NickDataManager manager = MHDFToolsAPI.getInstance().getNickDataManager();
        if (!manager.isEnable()) throw new IllegalStateException("匿名功能未开启");
        return manager.get(this);
    }

    @Override
    public void setNick(String name) {
        NickDataManager manager = MHDFToolsAPI.getInstance().getNickDataManager();
        if (!manager.isEnable()) throw new IllegalStateException("匿名功能未开启");

        NickData data = this.getNickData();
        data.setNick(name);
        manager.update(data);
        this.setNickDisplay(ColorUtil.color(name));
    }

    @Override
    public void deleteNick() {
        NickDataManager manager = MHDFToolsAPI.getInstance().getNickDataManager();
        if (!manager.isEnable()) throw new IllegalStateException("匿名功能未开启");
        manager.delete(this.getNickData());
        this.setNickDisplay(null);
    }

    @Override
    public void showNickDisplay() {
        NickDataManager manager = MHDFToolsAPI.getInstance().getNickDataManager();
        if (!manager.isEnable()) return;

        NickData data = manager.get(this);
        if (data.getNick() == null) return;
        this.setNickDisplay(ColorUtil.color(data.getNick()));
    }

    @Override
    public boolean isEnableVanish() {
        VanishStatusManager manager = MHDFToolsAPI.getInstance().getVanishStatusManager();
        return manager.isEnable() && manager.isEnable(this);
    }

    @Override
    public VanishStatus getVanishStatus() {
        VanishStatusManager manager = MHDFToolsAPI.getInstance().getVanishStatusManager();
        if (!manager.isEnable()) throw new IllegalStateException("隐身功能未开启");
        return manager.get(this);
    }

    @Override
    public void enableVanish() {
        VanishStatusManager manager = MHDFToolsAPI.getInstance().getVanishStatusManager();
        if (!manager.isEnable()) throw new IllegalStateException("隐身功能未开启");

        VanishStatus status = this.getVanishStatus();
        status.setEnable(true);
        manager.update(status);
        this.hidePlayer();
    }

    @Override
    public void hidePlayer() {
        if (!MHDFToolsAPI.getInstance().getVanishStatusManager().isEnable())
            throw new IllegalStateException("隐身功能未开启");

        Player player = this.getPlayer();
        if (player == null) return;

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(Main.instance, player);
        }
    }

    @Override
    public void disableVanish() {
        VanishStatusManager manager = MHDFToolsAPI.getInstance().getVanishStatusManager();
        if (!manager.isEnable()) throw new IllegalStateException("隐身功能未开启");

        VanishStatus status = this.getVanishStatus();
        status.setEnable(false);
        manager.update(status);
        this.showPlayer();
    }

    @Override
    public void showPlayer() {
        if (!MHDFToolsAPI.getInstance().getVanishStatusManager().isEnable())
            throw new IllegalStateException("隐身功能未开启");

        Player player = this.getPlayer();
        if (player == null) return;

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(Main.instance, player);
        }
    }

    @Override
    public List<BackData> getBackDataList(int amount) {
        BackDataManager manager = MHDFToolsAPI.getInstance().getBackDataManager();
        if (!manager.isEnable()) return List.of();
        return manager.getList(this, amount);
    }

    @Override
    public List<BackData> getBackDataList(String type, int amount) {
        BackDataManager manager = MHDFToolsAPI.getInstance().getBackDataManager();
        if (!manager.isEnable()) return List.of();
        return manager.getList(this, type, amount);
    }

    @Override
    public BackData getBackData(int id) {
        BackDataManager manager = MHDFToolsAPI.getInstance().getBackDataManager();
        if (!manager.isEnable()) throw new IllegalStateException("位置记录功能未开启");
        return manager.getById(id);
    }

    @Override
    public void addBack(String type, BungeeCordLocation location) {
        BackDataManager manager = MHDFToolsAPI.getInstance().getBackDataManager();
        if (!manager.isEnable()) throw new IllegalStateException("位置记录功能未开启");
        manager.update(new BackData(this, type, location));
    }

    @Override
    public boolean isEnablePvp() {
        PvpStatusManager manager = MHDFToolsAPI.getInstance().getPvpStatusManager();
        if (!manager.isEnable()) return manager.getDefaultValue();
        return manager.isEnable(this);
    }

    @Override
    public PvpStatus getPvpStatus() {
        PvpStatusManager manager = MHDFToolsAPI.getInstance().getPvpStatusManager();
        if (!manager.isEnable()) throw new IllegalStateException("PVP功能未开启");
        return manager.get(this);
    }

    @Override
    public void enablePvp() {
        PvpStatusManager manager = MHDFToolsAPI.getInstance().getPvpStatusManager();
        if (!manager.isEnable()) throw new IllegalStateException("PVP功能未开启");

        PvpStatus status = this.getPvpStatus();
        status.setEnable(true);
        manager.update(status);
    }

    @Override
    public void disablePvp() {
        PvpStatusManager manager = MHDFToolsAPI.getInstance().getPvpStatusManager();
        if (!manager.isEnable()) throw new IllegalStateException("PVP功能未开启");

        PvpStatus status = this.getPvpStatus();
        status.setEnable(false);
        manager.update(status);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MHDFToolsPlayerImpl that)) return false;
        return Objects.equals(uuid, that.uuid) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name);
    }

    @Override
    public boolean equals(MHDFToolsPlayer target) {
        return this.uuid.equals(target.getUuid());
    }
}
