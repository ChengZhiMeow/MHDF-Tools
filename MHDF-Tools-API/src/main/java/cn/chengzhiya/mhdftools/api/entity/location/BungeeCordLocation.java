package cn.chengzhiya.mhdftools.api.entity.location;

import cn.chengzhiya.mhdftools.api.MHDFToolsAPIHelper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
@Setter
public final class BungeeCordLocation {
    private String server;
    private String world;
    private Double x;
    private Double y;
    private Double z;
    private Float yaw;
    private Float pitch;

    public BungeeCordLocation(String server, String world, Double x, Double y, Double z, Float yaw, Float pitch) {
        this.server = server;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public BungeeCordLocation(String world, Double x, Double y, Double z, Float yaw, Float pitch) {
        this(
                MHDFToolsAPIHelper.getInstance().getServerName(),
                world,
                x,
                y,
                z,
                yaw,
                pitch
        );
    }

    public BungeeCordLocation(String server, Location location) {
        this(
                server,
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    public BungeeCordLocation(Location location) {
        this(
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    public BungeeCordLocation(String string) {
        String[] data = string.split(":");
        this.server = data[0];
        this.world = data[1];
        this.x = Double.parseDouble(data[2]);
        this.y = Double.parseDouble(data[3]);
        this.z = Double.parseDouble(data[4]);
        this.yaw = Float.parseFloat(data[5]);
        this.pitch = Float.parseFloat(data[6]);
    }

    /**
     * 转换为位置实例
     *
     * @return 位置实例
     */
    public Location toLocation() {
        return new Location(
                Bukkit.getWorld(getWorld()),
                getX(),
                getY(),
                getZ(),
                getYaw(),
                getPitch()
        );
    }

    /**
     * 转换为base64字符串
     *
     * @return base64字符串
     */
    public String toString() {
        return server + ":" + world + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch;
    }
}
