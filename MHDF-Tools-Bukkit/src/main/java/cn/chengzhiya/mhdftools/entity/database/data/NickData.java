package cn.chengzhiya.mhdftools.entity.database.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@DatabaseTable(tableName = "mhdftools_nick")
public final class NickData {
    @DatabaseField(id = true, canBeNull = false)
    private UUID player;
    @DatabaseField
    private String nick;
}
