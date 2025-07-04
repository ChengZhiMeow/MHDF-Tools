package cn.chengzhiya.mhdftools.api.entity.database.data;

import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@DatabaseTable(tableName = "mhdftools_vanish")
public final class VanishStatus extends Dao {
    @DatabaseField(id = true, canBeNull = false)
    private UUID player;
    @DatabaseField(columnName = "isEnable", canBeNull = false)
    private boolean enable;

    public VanishStatus() {
    }

    public VanishStatus(MHDFToolsPlayer player, boolean enable) {
        this.player = player.getUuid();
        this.enable = enable;
    }

    public VanishStatus(MHDFToolsPlayer player) {
        this(
                player,
                false
        );
    }
}
