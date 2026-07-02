package cn.chengzhimeow.mhdftools.bukkit.module.chat.cache;

import cn.chengzhimeow.mhdftools.bukkit.api.MHDFToolsBukkit;
import net.nyana.cache.service.CacheService;
import net.nyana.nbt.NBT;
import net.nyana.nbt.tag.CompoundTag;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public final class DisplayCache {
    public static byte[] encode(CacheEntry entry, int removeCache) {
        CompoundTag root = NBT.createCompound();
        root.putString("id", entry.id());
        root.putString("type", entry.type().name());
        root.putInt("remove_cache", removeCache);
        root.putByteArray("payload", entry.data());
        try {
            return NBT.toBytes(root);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to encode display cache", exception);
        }
    }

    public static ItemStack readItem(CacheEntry entry) {
        if (entry.type() != Type.ITEM) return null;

        try {
            return ItemStack.deserializeBytes(entry.data());
        } catch (Exception ignored) {
            return null;
        }
    }

    public static Map<Integer, ItemStack> readSlottedItems(CacheEntry entry) {
        Map<Integer, ItemStack> items = new LinkedHashMap<>();

        try {
            CompoundTag root = NBT.fromBytes(entry.data());
            for (String key : root.keySet()) {
                byte[] data = root.getByteArray(key);
                if (data.length == 0) continue;
                items.put(Integer.parseInt(key), ItemStack.deserializeBytes(data));
            }
        } catch (Exception ignored) {
        }
        return items;
    }

    private static CacheEntry decode(byte[] data) throws IOException {
        CompoundTag root = NBT.fromBytes(data);
        String id = root.getString("id");
        Type type = Type.valueOf(root.getString("type"));
        byte[] payload = root.getByteArray("payload", new byte[0]);
        return new CacheEntry(id, type, payload);
    }

    private static int decodeRemoveCache(byte[] data) throws IOException {
        return NBT.fromBytes(data).getInt("remove_cache", 60);
    }

    private CacheService<String, byte[]> cache;

    public void init() {
        this.cache = MHDFToolsBukkit.getInstance().getCacheManager().createCache("module:chat:display", byte[].class);
        this.cache.init();
    }

    public void close() {
        if (!(this.cache instanceof AutoCloseable closeable)) return;

        try {
            closeable.close();
        } catch (Exception ignored) {
        }
    }

    public CacheEntry putItem(ItemStack item, int removeCache) {
        return put(Type.ITEM, item.serializeAsBytes(), removeCache);
    }

    public CacheEntry putInventory(Type type, ItemStack[] contents, int removeCache) {
        CompoundTag root = NBT.createCompound();
        for (int slot = 0; slot < contents.length; slot++) {
            ItemStack item = contents[slot];
            if (item == null || item.getType().isAir()) continue;
            root.putByteArray(String.valueOf(slot), item.serializeAsBytes());
        }

        try {
            return put(type, NBT.toBytes(root), removeCache);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to encode inventory cache", exception);
        }
    }

    public CacheEntry get(String id) {
        byte[] data = this.cache.get(id);
        if (data == null) return null;

        try {
            return decode(data);
        } catch (Exception ignored) {
            return null;
        }
    }

    public void putEncoded(byte[] data) {
        try {
            CacheEntry entry = decode(data);
            int removeCache = decodeRemoveCache(data);
            this.cache.put(entry.id(), data, (long) Math.max(removeCache, 1));
        } catch (Exception ignored) {
        }
    }

    private CacheEntry put(Type type, byte[] data, int removeCache) {
        return put(UUID.randomUUID().toString(), type, data, removeCache);
    }

    private CacheEntry put(String id, Type type, byte[] data, int removeCache) {
        CacheEntry entry = new CacheEntry(id, type, data);
        this.cache.put(id, encode(entry, removeCache), (long) Math.max(removeCache, 1));
        return entry;
    }

    public enum Type {
        ITEM,
        INVENTORY,
        ENDER_CHEST
    }

    public record CacheEntry(String id, Type type, byte[] data) {
    }
}
