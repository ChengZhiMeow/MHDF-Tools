package cn.chengzhiya.mhdftools.api.entity.database.data;

import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@DatabaseTable(tableName = "mhdftools_fly")
public final class FlyStatus extends Dao {
    @DatabaseField(id = true, canBeNull = false)
    private UUID player;
    @DatabaseField(canBeNull = false)
    private boolean enable;
    @DatabaseField(canBeNull = false)
    private long time;

    public FlyStatus() {
    }

    public FlyStatus(MHDFToolsPlayer player, boolean enable, long time) {
        this.player = player.getUuid();
        this.enable = enable;
        this.time = time;
    }

    public FlyStatus(MHDFToolsPlayer player, boolean enable) {
        this(
                player,
                enable,
                -1
        );
    }

    public FlyStatus(MHDFToolsPlayer player) {
        this(
                player,
                false
        );
    }
}
