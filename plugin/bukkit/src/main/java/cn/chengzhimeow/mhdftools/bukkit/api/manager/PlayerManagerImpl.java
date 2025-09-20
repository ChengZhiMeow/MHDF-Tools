package cn.chengzhimeow.mhdftools.bukkit.api.manager;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.manager.PlayerManager;
import cn.chengzhimeow.mhdftools.bukkit.api.entity.MHDFToolsPlayerImpl;
import lombok.Getter;
import org.bukkit.OfflinePlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class PlayerManagerImpl implements PlayerManager {
    private final Map<UUID, MHDFToolsPlayer> playerHashMap = new ConcurrentHashMap<>();

    @Override
    public MHDFToolsPlayer getPlayer(UUID uuid) {
        return this.getPlayerHashMap().getOrDefault(uuid, new MHDFToolsPlayerImpl(uuid));
    }

    @Override
    public MHDFToolsPlayer getPlayer(String name) {
        return this.getPlayer(MHDFToolsAPIHelper.getInstance().getPlayerDataManager().get(name).getPlayer());
    }

    @Override
    public MHDFToolsPlayer getPlayer(OfflinePlayer player) {
        return this.getPlayer(player.getUniqueId());
    }
}
