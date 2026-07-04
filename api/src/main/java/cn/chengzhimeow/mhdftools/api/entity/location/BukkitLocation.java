package cn.chengzhimeow.mhdftools.api.entity.location;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class BukkitLocation {
    private String world;
    private Double x;
    private Double y;
    private Double z;
    private Float yaw;
    private Float pitch;

    public BukkitLocation(String world, Double x, Double y, Double z, Float yaw, Float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public BukkitLocation(String string) {
        String[] data = string.split(":");
        this.world = data[0];
        this.x = Double.parseDouble(data[1]);
        this.y = Double.parseDouble(data[2]);
        this.z = Double.parseDouble(data[3]);
        this.yaw = Float.parseFloat(data[4]);
        this.pitch = Float.parseFloat(data[5]);
    }

    public String toString() {
        return this.world + ":" + this.x + ":" + this.y + ":" + this.z + ":" + this.yaw + ":" + this.pitch;
    }
}
