package cn.chengzhimeow.mhdftools.bukkit.api.database.serializer;

import cn.chengzhimeow.mhdftools.api.entity.database.data.PvpStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class PvpStatusSerializer extends DataSerializer<PvpStatus> {
    @Override
    protected void write(DataOutputStream out, PvpStatus value) throws IOException {
        this.writeUuid(out, value.getPlayer());
        out.writeBoolean(value.isEnable());
    }

    @Override
    protected PvpStatus read(DataInputStream in) throws IOException {
        PvpStatus value = new PvpStatus();
        value.setPlayer(this.readUuid(in));
        value.setEnable(in.readBoolean());
        return value;
    }
}
