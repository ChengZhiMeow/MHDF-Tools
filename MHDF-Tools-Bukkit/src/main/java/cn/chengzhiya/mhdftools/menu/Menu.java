package cn.chengzhiya.mhdftools.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface Menu {
    /**
     * 触发打开菜单事件的时候
     *
     * @param event 触发打开菜单事件
     */
    void open(InventoryOpenEvent event);

    /**
     * 触发点击菜单事件的时候
     *
     * @param event 触发点击菜单事件
     */
    void click(InventoryClickEvent event);

    /**
     * 触发关闭菜单事件的时候
     *
     * @param event 触发关闭菜单事件
     */
    void close(InventoryCloseEvent event);
}
