package cn.chengzhiya.mhdftools.entity.database.data.huskhomes;

import com.j256.ormlite.field.DatabaseField;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public final class HuskHomesWarpData {
    @DatabaseField(canBeNull = false)
    private UUID uuid;
    @DatabaseField(canBeNull = false, columnName = "saved_position_id")
    private int positionInfoId;
}
