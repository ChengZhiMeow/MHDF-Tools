package cn.chengzhiya.mhdftools.builder;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public final class ItemStackBuilder {
    @Getter
    private final Player player;
    private final ItemStack item;

    public ItemStackBuilder(Player player, String type) {
        this.player = player;

        ItemStack item = null;
        if (type != null) {
            if (type.startsWith("craftEngine-")) {
                item = Main.instance.getPluginHookManager().getCraftEngineHook().getItem(
                        type.replace("craftEngine-", "")
                );
            } else if (type.startsWith("mythicMobs-")) {
                item = Main.instance.getPluginHookManager().getMythicMobsHook().getItem(
                        type.replace("mythicMobs-", "")
                );
            } else if (type.startsWith("head-")) {
                item = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) item.getItemMeta();

                String playerName = type.replace("head-", "");
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));

                item.setItemMeta(meta);
            } else if (type.equals("random_bed")) {
                item = new ItemStack(this.getRandomBed());
            } else {
                Material material = Material.matchMaterial(type);
                if (material != null) {
                    item = new ItemStack(material);
                }
            }
        }

        this.item = Objects.requireNonNullElse(item, new ItemStack(Material.AIR));
    }

    public ItemStackBuilder(Player player, ItemStack item) {
        this.player = player;
        this.item = item;
    }

    public ItemStackBuilder name(String name) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta == null) {
            return this;
        }
        if (name == null) {
            return this;
        }

        meta.displayName(ColorUtil.color(Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(getPlayer(), name)));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder lore(List<String> lore) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta == null) {
            return this;
        }
        if (lore == null || lore.isEmpty()) {
            return this;
        }

        meta.lore(lore.stream()
                .map(s -> Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(getPlayer(), s))
                .map(ColorUtil::color)
                .toList()
        );
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder customModelData(Integer customModelData) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta == null) {
            return this;
        }
        if (customModelData == null || customModelData <= 0) {
            return this;
        }

        meta.setCustomModelData(customModelData);
        this.item.setItemMeta(meta);
        return this;
    }

    public <P, C> ItemStackBuilder persistentDataContainer(String key, PersistentDataType<P, C> type, C value) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta == null) {
            return this;
        }
        if (key == null || type == null || value == null) {
            return this;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(new NamespacedKey(Main.instance, key), type, value);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder amount(Integer amount) {
        if (amount == null || amount <= 0) {
            return this;
        }

        this.item.setAmount(amount);
        return this;
    }

    /**
     * 获取随机床空间ID实例
     *
     * @return 空间ID实例
     */
    private Material getRandomBed() {
        List<Material> bedList = Arrays.asList(
                Material.BLACK_BED,
                Material.BLUE_BED,
                Material.BROWN_BED,
                Material.CYAN_BED,
                Material.GREEN_BED,
                Material.LIGHT_BLUE_BED,
                Material.LIGHT_GRAY_BED,
                Material.MAGENTA_BED,
                Material.ORANGE_BED,
                Material.LIME_BED,
                Material.PINK_BED,
                Material.PURPLE_BED,
                Material.RED_BED,
                Material.WHITE_BED,
                Material.YELLOW_BED
        );
        return bedList.get(new Random().nextInt(bedList.size()));
    }

    /**
     * 构建物品实例
     *
     * @return 物品实例
     */
    public ItemStack build() {
        return this.item;
    }
}
