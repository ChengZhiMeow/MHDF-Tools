package cn.chengzhimeow.mhdftools.bukkit.api.database.serializer;

import cn.chengzhimeow.mhdftools.api.entity.database.data.PlayerData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class PlayerDataSerializer extends DataSerializer<PlayerData> {
    @Override
    protected void write(DataOutputStream out, PlayerData value) throws IOException {
        this.writeUuid(out, value.getPlayer());
        this.writeString(out, value.getName());
    }

    @Override
    protected PlayerData read(DataInputStream in) throws IOException {
        return new PlayerData(this.readUuid(in), this.readString(in));
    }
}
