package cn.chengzhimeow.mhdftools.bukkit.api.database.serializer;

import cn.chengzhimeow.mhdftools.api.entity.database.data.HomeData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class HomeDataSerializer extends DataSerializer<HomeData> {
    @Override
    protected void write(DataOutputStream out, HomeData value) throws IOException {
        out.writeInt(value.getId() == null ? 0 : value.getId());
        this.writeUuid(out, value.getPlayer());
        this.writeString(out, value.getHome());
        this.writeString(out, value.getServer());
        this.writeString(out, value.getWorld());
        out.writeDouble(value.getX());
        out.writeDouble(value.getY());
        out.writeDouble(value.getZ());
        out.writeFloat(value.getYaw());
        out.writeFloat(value.getPitch());
    }

    @Override
    protected HomeData read(DataInputStream in) throws IOException {
        HomeData value = new HomeData();
        value.setId(in.readInt());
        value.setPlayer(this.readUuid(in));
        value.setHome(this.readString(in));
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
