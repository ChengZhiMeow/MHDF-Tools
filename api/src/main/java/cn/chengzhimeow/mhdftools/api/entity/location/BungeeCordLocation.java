package cn.chengzhimeow.mhdftools.api.entity.location;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class BungeeCordLocation {
    private String server;
    private BukkitLocation location;

    public BungeeCordLocation(String server, BukkitLocation location) {
        this.server = server;
        this.location = location;
    }

    public BungeeCordLocation(BukkitLocation location) {
        this(MHDFToolsAPI.getInstance().getServerName(), location);
    }

    public BungeeCordLocation(String server, String world, Double x, Double y, Double z, Float yaw, Float pitch) {
        this.server = server;
        this.location = new BukkitLocation(world, x, y, z, yaw, pitch);
    }

    public BungeeCordLocation(String world, Double x, Double y, Double z, Float yaw, Float pitch) {
        this(
                MHDFToolsAPI.getInstance().getServerName(),
                new BukkitLocation(world, x, y, z, yaw, pitch)
        );
    }

    public BungeeCordLocation(String string) {
        String[] data = string.split(":");
        this.server = data[0];
        this.location = new BukkitLocation(
                data[1],
                Double.parseDouble(data[2]),
                Double.parseDouble(data[3]),
                Double.parseDouble(data[4]),
                Float.parseFloat(data[5]),
                Float.parseFloat(data[6])
        );
    }

    public String getWorld() {
        return this.location.getWorld();
    }

    public void setWorld(String world) {
        this.location.setWorld(world);
    }

    public Double getX() {
        return this.location.getX();
    }

    public void setX(Double x) {
        this.location.setX(x);
    }

    public Double getY() {
        return this.location.getY();
    }

    public void setY(Double y) {
        this.location.setY(y);
    }

    public Double getZ() {
        return this.location.getZ();
    }

    public void setZ(Double z) {
        this.location.setZ(z);
    }

    public Float getYaw() {
        return this.location.getYaw();
    }

    public void setYaw(Float yaw) {
        this.location.setYaw(yaw);
    }

    public Float getPitch() {
        return this.location.getPitch();
    }

    public void setPitch(Float pitch) {
        this.location.setPitch(pitch);
    }

    /**
     * 转换为base64字符串
     *
     * @return base64字符串
     */
    public String toString() {
        return this.server + ":" + this.location;
    }
}
