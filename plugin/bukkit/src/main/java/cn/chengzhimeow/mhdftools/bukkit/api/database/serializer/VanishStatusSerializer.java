package cn.chengzhimeow.mhdftools.bukkit.api.database.serializer;

import cn.chengzhimeow.mhdftools.api.entity.database.data.VanishStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class VanishStatusSerializer extends DataSerializer<VanishStatus> {
    @Override
    protected void write(DataOutputStream out, VanishStatus value) throws IOException {
        this.writeUuid(out, value.getPlayer());
        out.writeBoolean(value.isEnable());
    }

    @Override
    protected VanishStatus read(DataInputStream in) throws IOException {
        VanishStatus value = new VanishStatus();
        value.setPlayer(this.readUuid(in));
        value.setEnable(in.readBoolean());
        return value;
    }
}
