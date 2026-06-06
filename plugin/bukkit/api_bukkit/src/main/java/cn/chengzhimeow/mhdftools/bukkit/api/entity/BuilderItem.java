package cn.chengzhimeow.mhdftools.bukkit.api.entity;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuilderItem {
    private final String id;
    private String by;
    private String type;
    private Component name;
    private List<Component> lore;
    private Integer customModelData;
    private int amount;
    private Map<String, String> pdc;

    public BuilderItem(
            @NotNull String id,
            @NotNull String by,
            @NotNull String type,
            @Nullable Component name,
            @NotNull List<Component> lore,
            @Nullable Integer customModelData,
            int amount,
            @NotNull Map<String, String> pdc
    ) {
        this.id = id;
        this.by = by;
        this.type = type;
        this.name = name;
        this.lore = lore;
        this.customModelData = customModelData;
        this.amount = amount;
        this.pdc = pdc;
    }

    public @NotNull BuilderItem copy() {
        return new BuilderItem(
                this.id,
                this.by,
                this.type,
                this.name,
                new ArrayList<>(this.lore),
                this.customModelData,
                this.amount,
                new HashMap<>(this.pdc)
        );
    }

    public @NotNull String getId() {
        return this.id;
    }

    public @NotNull String getBy() {
        return this.by;
    }

    public void setBy(@NotNull String by) {
        this.by = by;
    }

    public @NotNull String getType() {
        return this.type;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }

    public @Nullable Component getName() {
        return this.name;
    }

    public void setName(@Nullable Component name) {
        this.name = name;
    }

    public @NotNull List<Component> getLore() {
        return this.lore;
    }

    public void setLore(@NotNull List<Component> lore) {
        this.lore = lore;
    }

    public @Nullable Integer getCustomModelData() {
        return this.customModelData;
    }

    public void setCustomModelData(@Nullable Integer customModelData) {
        this.customModelData = customModelData;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public @NotNull Map<String, String> getPdc() {
        return this.pdc;
    }

    public void setPdc(@NotNull Map<String, String> pdc) {
        this.pdc = pdc;
    }
}
