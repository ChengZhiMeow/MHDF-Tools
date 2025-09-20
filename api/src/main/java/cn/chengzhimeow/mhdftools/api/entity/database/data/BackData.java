package cn.chengzhimeow.mhdftools.api.entity.database.data;

import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.api.entity.database.Dao;
import cn.chengzhimeow.mhdftools.api.entity.location.BungeeCordLocation;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@DatabaseTable(tableName = "mhdftools_back")
public final class BackData extends Dao {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(index = true, canBeNull = false)
    private UUID player;
    @DatabaseField(index = true, canBeNull = false)
    private String type;
    @DatabaseField(canBeNull = false)
    private long time = System.currentTimeMillis();
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

    public BackData() {
    }

    public BackData(MHDFToolsPlayer player, String type) {
        this.player = player.getUuid();
        this.type = type;
    }

    public BackData(MHDFToolsPlayer player, String type, BungeeCordLocation location) {
        this(
                player,
                type
        );
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