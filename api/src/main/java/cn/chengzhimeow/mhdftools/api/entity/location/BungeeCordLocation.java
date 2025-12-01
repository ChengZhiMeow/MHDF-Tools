package cn.chengzhimeow.mhdftools.api.entity.location;

import lombok.Getter;
import lombok.Setter;

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
                MHDFToolsAPI.getInstance().getServerName(),
                world,
                x,
                y,
                z,
                yaw,
                pitch
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
     * 转换为base64字符串
     *
     * @return base64字符串
     */
    public String toString() {
        return this.server + ":" + this.world + ":" + this.x + ":" + this.y + ":" + this.z + ":" + this.yaw + ":" + this.pitch;
    }
}
