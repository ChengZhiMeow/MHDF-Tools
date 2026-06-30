package cn.chengzhimeow.mhdftools.bukkit.common.menu.item;

import cn.chengzhimeow.cccondition.condition.ConditionBuilder;
import cn.chengzhimeow.mhdftools.bukkit.api.entity.BuilderItem;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionAction;
import cn.chengzhimeow.mhdftools.bukkit.common.action.ConditionActionManager;
import cn.chengzhimeow.mhdftools.bukkit.common.condition.ConditionManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MenuItem extends BuilderItem {
    private String slot;
    private List<ConditionBuilder.Builder> conditions;
    private Map<MenuActionType, List<ConditionAction>> actions;

    public MenuItem(
            @NotNull String id,
            @Nullable String slot,
            @NotNull String by,
            @NotNull String type,
            @Nullable Component name,
            @NotNull List<Component> lore,
            @Nullable Integer customModelData,
            int amount,
            @NotNull Map<String, String> pdc,
            @NotNull List<ConditionBuilder.Builder> conditions,
            @NotNull Map<MenuActionType, List<ConditionAction>> actions
    ) {
        super(id, by, type, name, lore, customModelData, amount, pdc);
        this.slot = slot;
        this.conditions = conditions;
        this.actions = actions;
    }

    public boolean checkConditions(@Nullable Player player) {
        return this.checkConditions(player, new HashMap<>());
    }

    public boolean checkConditions(@Nullable Player player, @NotNull Map<String, Object> params) {
        return ConditionManager.getInstance().condition(player, this.conditions, params);
    }

    public void action(@Nullable Player player, @NotNull ClickType clickType) {
        this.action(player, clickType, new HashMap<>());
    }

    public void action(@Nullable Player player, @NotNull ClickType clickType, @NotNull Map<String, Object> params) {
        this.actions.forEach((type, actions) -> {
            if (!type.matches(clickType)) return;
            for (ConditionAction action : actions) {
                ConditionActionManager.getInstance().actionWithCondition(player, action, params);
            }
        });
    }

    public @NotNull MenuItem copy() {
        return new MenuItem(
                this.getId(),
                this.slot,
                this.getBy(),
                this.getType(),
                this.getName(),
                new ArrayList<>(this.getLore()),
                this.getCustomModelData(),
                this.getAmount(),
                new HashMap<>(this.getPdc()),
                new ArrayList<>(this.conditions),
                new EnumMap<>(this.actions)
        );
    }

    public @Nullable String getSlot() {
        return this.slot;
    }

    public void setSlot(@Nullable String slot) {
        this.slot = slot;
    }

    public @NotNull List<ConditionBuilder.Builder> getConditions() {
        return this.conditions;
    }

    public void setConditions(@NotNull List<ConditionBuilder.Builder> conditions) {
        this.conditions = conditions;
    }

    public @NotNull Map<MenuActionType, List<ConditionAction>> getActions() {
        return this.actions;
    }

    public void setActions(@NotNull Map<MenuActionType, List<ConditionAction>> actions) {
        this.actions = actions;
    }
}
