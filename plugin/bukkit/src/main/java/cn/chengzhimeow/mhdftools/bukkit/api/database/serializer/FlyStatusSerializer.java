package cn.chengzhimeow.mhdftools.bukkit.api.database.serializer;

import cn.chengzhimeow.mhdftools.api.entity.database.data.FlyStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class FlyStatusSerializer extends DataSerializer<FlyStatus> {
    @Override
    protected void write(DataOutputStream out, FlyStatus value) throws IOException {
        this.writeUuid(out, value.getPlayer());
        out.writeBoolean(value.isEnable());
        out.writeLong(value.getTime());
    }

    @Override
    protected FlyStatus read(DataInputStream in) throws IOException {
        FlyStatus value = new FlyStatus();
        value.setPlayer(this.readUuid(in));
        value.setEnable(in.readBoolean());
        value.setTime(in.readLong());
        return value;
    }
}
