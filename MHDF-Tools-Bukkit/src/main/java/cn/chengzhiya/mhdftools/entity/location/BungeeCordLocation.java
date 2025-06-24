package cn.chengzhiya.mhdftools.entity.location;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.Base64Util;
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
                Main.instance.getBungeeCordManager().getServerName(),
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
                Main.instance.getBungeeCordManager().getServerName(),
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    public BungeeCordLocation(String base64) {
        String[] data = new String(Base64Util.decode(base64)).split(":");
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
    public String toBase64() {
        return Base64Util.encode(server + ":" + world + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch);
    }
}
