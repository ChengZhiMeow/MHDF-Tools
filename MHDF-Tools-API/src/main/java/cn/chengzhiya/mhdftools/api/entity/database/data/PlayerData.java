package cn.chengzhiya.mhdftools.api.entity.database.data;

import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.Dao;
import com.j256.ormlite.field.DatabaseField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

@Getter
@Setter
@ToString
public final class PlayerData extends Dao {
    @DatabaseField(id = true, canBeNull = false)
    private UUID player;
    @DatabaseField(index = true, canBeNull = false)
    private String name;

    public PlayerData() {
    }

    public PlayerData(UUID player) {
        this.player = player;
    }

    public PlayerData(UUID player, String name) {
        this(player);
        this.name = name;
    }

    public PlayerData(OfflinePlayer player) {
        this(
                player.getUniqueId(),
                player.getName()
        );
    }

    public PlayerData(MHDFToolsPlayer player) {
        this(
                player.getUuid(),
                player.getName()
        );
    }
}
