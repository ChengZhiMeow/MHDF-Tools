package cn.chengzhimeow.mhdftools.bukkit.api.database.serializer;

import cn.chengzhimeow.mhdftools.api.entity.database.data.WarpData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class WarpDataSerializer extends DataSerializer<WarpData> {
    @Override
    protected void write(DataOutputStream out, WarpData value) throws IOException {
        this.writeString(out, value.getWarp());
        this.writeString(out, value.getServer());
        this.writeString(out, value.getWorld());
        out.writeDouble(value.getX());
        out.writeDouble(value.getY());
        out.writeDouble(value.getZ());
        out.writeFloat(value.getYaw());
        out.writeFloat(value.getPitch());
    }

    @Override
    protected WarpData read(DataInputStream in) throws IOException {
        WarpData value = new WarpData();
        value.setWarp(this.readString(in));
        value.setServer(this.readString(in));
        value.setWorld(this.readString(in));
        value.setX(in.readDouble());
        value.setY(in.readDouble());
        value.setZ(in.readDouble());
        value.setYaw(in.readFloat());
        value.setPitch(in.readFloat());
        return value;
    }
}
