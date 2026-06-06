package cn.chengzhimeow.mhdftools.bukkit.api.database.serializer;

import cn.chengzhimeow.mhdftools.api.entity.database.data.IgnoreData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class IgnoreDataSerializer extends DataSerializer<IgnoreData> {
    @Override
    protected void write(DataOutputStream out, IgnoreData value) throws IOException {
        out.writeInt(value.getId() == null ? 0 : value.getId());
        this.writeUuid(out, value.getPlayer());
        this.writeUuid(out, value.getIgnore());
    }

    @Override
    protected IgnoreData read(DataInputStream in) throws IOException {
        IgnoreData value = new IgnoreData();
        value.setId(in.readInt());
        value.setPlayer(this.readUuid(in));
        value.setIgnore(this.readUuid(in));
        return value;
    }
}
