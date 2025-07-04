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
@DatabaseTable(tableName = "mhdftools_nick")
public final class NickData extends Dao {
    @DatabaseField(id = true, canBeNull = false)
    private UUID player;
    @DatabaseField
    private String nick;

    public NickData() {
    }

    public NickData(MHDFToolsPlayer player, String nick) {
        this.player = player.getUuid();
        this.nick = nick;
    }

    public NickData(MHDFToolsPlayer player) {
        this(
                player,
                null
        );
    }
}
