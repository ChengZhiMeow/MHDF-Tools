package cn.chengzhiya.mhdftools.api.entity.database.data;

import cn.chengzhiya.mhdftools.api.entity.database.Dao;
import cn.chengzhiya.mhdftools.api.entity.location.BungeeCordLocation;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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

    public WarpData() {
    }

    public WarpData(String name) {
        this.warp = name;
    }

    public WarpData(String name, BungeeCordLocation location) {
        this(name);
        this.setLocation(location);
    }

    public void setLocation(BungeeCordLocation location) {
        this.setServer(location.getServer());
        this.setWorld(location.getWorld());
        this.setX(location.getX());
        this.setY(location.getY());
        this.setZ(location.getZ());
        this.setYaw(location.getYaw());
        this.setPitch(location.getPitch());
    }

    /**
     * 转换为群组位置实例
     *
     * @return 群组位置实例
     */
    public BungeeCordLocation toBungeeCordLocation() {
        return new BungeeCordLocation(
                this.getServer(),
                this.getWorld(),
                this.getX(),
                this.getY(),
                this.getZ(),
                this.getYaw(),
                this.getPitch()
        );
    }
}
