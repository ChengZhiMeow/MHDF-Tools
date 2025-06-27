package cn.chengzhiya.mhdftools.entity.database.data;

import cn.chengzhiya.mhdftools.entity.database.Dao;
import cn.chengzhiya.mhdftools.entity.location.BungeeCordLocation;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DatabaseTable(tableName = "mhdftools_warp")
public final class WarpData extends Dao {
    @DatabaseField(id = true, canBeNull = false)
    private String warp;
    @DatabaseField(canBeNull = false)
    private String server;
    @DatabaseField(canBeNull = false)
    private String world;
    @DatabaseField(canBeNull = false)
    private Double x;
    @DatabaseField(canBeNull = false)
    private Double y;
    @DatabaseField(canBeNull = false)
    private Double z;
    @DatabaseField(canBeNull = false)
    private Float yaw;
    @DatabaseField(canBeNull = false)
    private Float pitch;

    public void setLocation(BungeeCordLocation bungeeCordLocation) {
        setServer(bungeeCordLocation.getServer());
        setWorld(bungeeCordLocation.getWorld());
        setX(bungeeCordLocation.getX());
        setY(bungeeCordLocation.getY());
        setZ(bungeeCordLocation.getZ());
        setYaw(bungeeCordLocation.getYaw());
        setPitch(bungeeCordLocation.getPitch());
    }

    /**
     * 转换为群组位置实例
     *
     * @return 群组位置实例
     */
    public BungeeCordLocation toBungeeCordLocation() {
        return new BungeeCordLocation(
                getServer(),
                getWorld(),
                getX(),
                getY(),
                getZ(),
                getYaw(),
                getPitch()
        );
    }
}
