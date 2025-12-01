package cn.chengzhimeow.mhdftools.api.entity.database.data;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@DatabaseTable(tableName = "mhdftools_player")
public final class PlayerData extends Dao {
    @DatabaseField(id = true, canBeNull = false)
    private UUID player;
    @DatabaseField(index = true, canBeNull = false)
    private String name;

    public PlayerData() {
    }

    public PlayerData(UUID player, String name) {
        this.player = player;
        this.name = name;
    }

    public PlayerData(MHDFToolsPlayer player) {
        this(
                player.getUuid(),
                player.getName()
        );
    }
}
