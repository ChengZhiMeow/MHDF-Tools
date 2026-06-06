package cn.chengzhimeow.mhdftools.bukkit.common.menu.item;

import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

public enum MenuActionType {
    ALL,
    LEFT,
    LEFT_RIGHT,
    RIGHT,
    SHIFT_RIGHT,
    CLICK,
    SHIFT_CLICK;

    public boolean matches(@NotNull ClickType clickType) {
        return switch (this) {
            case ALL -> true;
            case LEFT -> clickType == ClickType.LEFT;
            case LEFT_RIGHT -> clickType == ClickType.LEFT || clickType == ClickType.RIGHT;
            case RIGHT -> clickType == ClickType.RIGHT;
            case SHIFT_RIGHT -> clickType == ClickType.SHIFT_RIGHT;
            case CLICK -> clickType == ClickType.LEFT || clickType == ClickType.RIGHT
                    || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT;
            case SHIFT_CLICK -> clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT;
        };
    }
}
