package cn.chengzhiya.mhdftools.entity.database.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@DatabaseTable(tableName = "mhdftools_fly")
public final class FlyStatus {
    @DatabaseField(id = true, canBeNull = false)
    private UUID player;
    @DatabaseField(canBeNull = false)
    private boolean isEnable;
    @DatabaseField(canBeNull = false)
    private long time;
}
