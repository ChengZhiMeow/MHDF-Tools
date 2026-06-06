package cn.chengzhimeow.mhdftools.bukkit.api.database.serializer;

import cn.chengzhimeow.mhdftools.api.entity.database.data.EconomyData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class EconomyDataSerializer extends DataSerializer<EconomyData> {
    @Override
    protected void write(DataOutputStream out, EconomyData value) throws IOException {
        this.writeUuid(out, value.getPlayer());
        this.writeBigDecimal(out, value.getMoney());
    }

    @Override
    protected EconomyData read(DataInputStream in) throws IOException {
        EconomyData value = new EconomyData();
        value.setPlayer(this.readUuid(in));
        value.setMoney(this.readBigDecimal(in));
        return value;
    }
}
