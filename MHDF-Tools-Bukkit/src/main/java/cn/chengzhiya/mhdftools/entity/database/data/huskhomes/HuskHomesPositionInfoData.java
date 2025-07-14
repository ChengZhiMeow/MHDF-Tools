package cn.chengzhiya.mhdftools.entity.database.data.huskhomes;

import com.j256.ormlite.field.DatabaseField;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public final class HuskHomesPositionInfoData {
    @DatabaseField(canBeNull = false)
    private int id;
    @DatabaseField(canBeNull = false, columnName = "position_id")
    private int positionId;
    @DatabaseField(canBeNull = false)
    private String name;
    @DatabaseField(canBeNull = false)
    private String description;
    @DatabaseField
    private Object tags;
    @DatabaseField(canBeNull = false)
    private Timestamp timestamp;
}
