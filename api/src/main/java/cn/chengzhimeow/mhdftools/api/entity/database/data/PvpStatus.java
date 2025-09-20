package cn.chengzhimeow.mhdftools.api.entity.database.data;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPIHelper;
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
@DatabaseTable(tableName = "mhdftools_pvp")
public final class PvpStatus extends Dao {
    @DatabaseField(id = true, canBeNull = false)
    private UUID player;
    @DatabaseField(columnName = "isEnable", canBeNull = false)
    private boolean enable;

    public PvpStatus() {
    }

    public PvpStatus(MHDFToolsPlayer player, boolean enable) {
        this.player = player.getUuid();
        this.enable = enable;
    }

    public PvpStatus(MHDFToolsPlayer player) {
        this(
                player,
                MHDFToolsAPIHelper.getInstance().getPvpStatusManager().getDefaultValue()
        );
    }
}
