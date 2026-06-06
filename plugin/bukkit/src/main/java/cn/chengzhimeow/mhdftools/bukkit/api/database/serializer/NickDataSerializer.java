package cn.chengzhimeow.mhdftools.bukkit.api.database.serializer;

import cn.chengzhimeow.mhdftools.api.entity.database.data.NickData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class NickDataSerializer extends DataSerializer<NickData> {
    @Override
    protected void write(DataOutputStream out, NickData value) throws IOException {
        this.writeUuid(out, value.getPlayer());
        this.writeString(out, value.getNick());
    }

    @Override
    protected NickData read(DataInputStream in) throws IOException {
        NickData value = new NickData();
        value.setPlayer(this.readUuid(in));
        value.setNick(this.readString(in));
        return value;
    }
}
