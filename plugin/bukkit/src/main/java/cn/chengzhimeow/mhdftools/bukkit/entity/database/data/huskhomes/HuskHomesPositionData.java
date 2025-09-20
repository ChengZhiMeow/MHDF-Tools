package cn.chengzhimeow.mhdftools.bukkit.entity.database.data.huskhomes;

import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import com.j256.ormlite.field.DatabaseField;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public final class HuskHomesPositionData {
    @DatabaseField(canBeNull = false)
    private int id;
    @DatabaseField(canBeNull = false)
    private double x;
    @DatabaseField(canBeNull = false)
    private double y;
    @DatabaseField(canBeNull = false)
    private double z;
    @DatabaseField(canBeNull = false)
    private float yaw;
    @DatabaseField(canBeNull = false)
    private float pitch;
    @DatabaseField(canBeNull = false, columnName = "world_name")
    private String worldName;
    @DatabaseField(canBeNull = false, columnName = "world_uuid")
    private UUID worldUuid;
    @DatabaseField(canBeNull = false, columnName = "server_name")
    private String server;

    /**
     * 转换为群组位置实例
     *
     * @return 群组位置实例
     */
    public BungeeCordLocation toBungeeCordLocation() {
        return new BungeeCordLocation(
                this.getServer(),
                this.getWorldName(),
                this.getX(),
                this.getY(),
                this.getZ(),
                this.getYaw(),
                this.getPitch()
        );
    }
}
