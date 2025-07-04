package cn.chengzhiya.mhdftools.api.entity.database.data;

import cn.chengzhiya.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhiya.mhdftools.api.entity.database.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@DatabaseTable(tableName = "mhdftools_economy")
public final class EconomyData extends Dao {
    @DatabaseField(id = true, canBeNull = false)
    private UUID player;
    @DatabaseField(format = "20,2", columnName = "bigDecimal", canBeNull = false)
    private BigDecimal money;

    public EconomyData() {
    }

    public EconomyData(MHDFToolsPlayer player, BigDecimal money) {
        this.player = player.getUuid();
        this.money = money;
    }

    public EconomyData(MHDFToolsPlayer player) {
        this(
                player,
                BigDecimal.ZERO
        );
    }
}
