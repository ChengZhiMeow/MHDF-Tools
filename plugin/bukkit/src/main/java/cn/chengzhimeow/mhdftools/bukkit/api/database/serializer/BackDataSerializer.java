package cn.chengzhimeow.mhdftools.bukkit.api.database.serializer;

import cn.chengzhimeow.mhdftools.api.entity.database.data.BackData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class BackDataSerializer extends DataSerializer<BackData> {
    @Override
    protected void write(DataOutputStream out, BackData value) throws IOException {
        out.writeInt(value.getId());
        this.writeUuid(out, value.getPlayer());
        this.writeString(out, value.getType());
        out.writeLong(value.getTime());
        this.writeString(out, value.getServer());
        this.writeString(out, value.getWorld());
        out.writeDouble(value.getX());
        out.writeDouble(value.getY());
        out.writeDouble(value.getZ());
        out.writeFloat(value.getYaw());
        out.writeFloat(value.getPitch());
    }

    @Override
    protected BackData read(DataInputStream in) throws IOException {
        BackData value = new BackData();
        value.setId(in.readInt());
        value.setPlayer(this.readUuid(in));
        value.setType(this.readString(in));
        value.setTime(in.readLong());
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
