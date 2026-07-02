package cn.chengzhimeow.mhdftools.bukkit.api.manager;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.data.PlayerData;
import cn.chengzhimeow.mhdftools.api.manager.PlayerManager;
import cn.chengzhimeow.mhdftools.bukkit.api.entity.MHDFToolsPlayerImpl;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class PlayerManagerImpl implements PlayerManager {
    private final Map<UUID, MHDFToolsPlayer> playerHashMap = new ConcurrentHashMap<>();

    @Override
    public MHDFToolsPlayer getPlayer(@NotNull UUID uuid, @Nullable String name) {
        return this.playerHashMap.computeIfAbsent(uuid, ignored -> new MHDFToolsPlayerImpl(uuid, name));
    }

    @Override
    public MHDFToolsPlayer getPlayer(@NotNull String name) {
        return this.getPlayer(MHDFToolsAPI.getInstance().getPlayerDataManager().get(name).getPlayer(), name);
    }

    @Override
    public @Nullable MHDFToolsPlayer getPlayerOrNull(String name) {
        PlayerData playerData = MHDFToolsAPI.getInstance().getPlayerDataManager().getOrNull(name);
        if (playerData == null) return null;
        return this.getPlayer(playerData.getPlayer(), name);
    }
}
