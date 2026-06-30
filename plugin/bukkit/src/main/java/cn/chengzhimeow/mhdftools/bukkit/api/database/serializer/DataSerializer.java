package cn.chengzhimeow.mhdftools.bukkit.api.database.serializer;

import net.nyana.cache.serialization.CacheSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.math.BigDecimal;
import java.util.UUID;

abstract class DataSerializer<T> implements CacheSerializer<T> {
    @Override
    public final byte @NotNull [] toBytes(@NotNull T value) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bytes);
            this.write(out, value);
            return bytes.toByteArray();
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    @Override
    public final @NotNull T byBytes(byte @NotNull [] bytes) {
        try {
            return this.read(new DataInputStream(new ByteArrayInputStream(bytes)));
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    protected abstract void write(DataOutputStream out, T value) throws IOException;

    protected abstract T read(DataInputStream in) throws IOException;

    protected void writeUuid(DataOutputStream out, UUID value) throws IOException {
        out.writeLong(value.getMostSignificantBits());
        out.writeLong(value.getLeastSignificantBits());
    }

    protected UUID readUuid(DataInputStream in) throws IOException {
        return new UUID(in.readLong(), in.readLong());
    }

    protected void writeString(DataOutputStream out, String value) throws IOException {
        out.writeBoolean(value != null);
        if (value != null) {
            out.writeUTF(value);
        }
    }

    protected String readString(DataInputStream in) throws IOException {
        return in.readBoolean() ? in.readUTF() : null;
    }

    protected void writeBigDecimal(DataOutputStream out, BigDecimal value) throws IOException {
        this.writeString(out, value == null ? null : value.toPlainString());
    }

    protected BigDecimal readBigDecimal(DataInputStream in) throws IOException {
        String value = this.readString(in);
        return value == null ? BigDecimal.ZERO : new BigDecimal(value);
    }
}
