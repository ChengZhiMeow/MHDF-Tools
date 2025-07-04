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
@DatabaseTable(tableName = "mhdftools_ignore")
public final class IgnoreData extends Dao {
    @DatabaseField(generatedId = true, canBeNull = false)
    private Integer id;
    @DatabaseField(canBeNull = false)
    private UUID player;
    @DatabaseField(canBeNull = false)
    private UUID ignore;

    public IgnoreData() {
    }

    public IgnoreData(MHDFToolsPlayer player, MHDFToolsPlayer ignore) {
        this.player = player.getUuid();
        this.ignore = ignore.getUuid();
    }
}
