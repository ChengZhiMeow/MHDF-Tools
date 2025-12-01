package cn.chengzhimeow.mhdftools.bukkit.menu;

import cn.chengzhimeow.ccscheduler.scheduler.CCScheduler;
import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.util.config.YamlUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Menu implements InventoryHolder {
    private final boolean enable;
    private final Player player;

    public Menu(List<String> enableKeyList, Player player) {
        this.enable = YamlUtil.equalsTrue(ConfigSetting.getInstance().getData(), enableKeyList);
        this.player = player;
    }

    public Menu(Player player) {
        this(new ArrayList<>(), player);
    }

    /**
     * 触发打开菜单事件的时候
     *
     * @param event 触发打开菜单事件
     */
    abstract public void open(InventoryOpenEvent event);

    /**
     * 触发打开菜单事件的时候
     *
     * @param event 触发打开菜单事件
     */
    public void onOpen(InventoryOpenEvent event) {
        if (!this.isEnable()) return;
        this.open(event);
    }

    /**
     * 触发点击菜单事件的时候
     *
     * @param event 触发点击菜单事件
     */
    abstract public void click(InventoryClickEvent event);

    /**
     * 触发点击菜单事件的时候
     *
     * @param event 触发点击菜单事件
     */
    public void onClick(InventoryClickEvent event) {
        if (!this.isEnable()) {
            return;
        }

        this.click(event);
    }

    /**
     * 触发关闭菜单事件的时候
     *
     * @param event 触发关闭菜单事件
     */
    public void onClose(InventoryCloseEvent event) {
        if (!this.isEnable()) {
            return;
        }

        this.close(event);
    }

    /**
     * 触发关闭菜单事件的时候
     *
     * @param event 触发关闭菜单事件
     */
    abstract public void close(InventoryCloseEvent event);

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (!this.isEnable()) {
            return;
        }

        CCScheduler.getInstance().getAsyncScheduler().runTask(Main.instance, () -> {
            Inventory menu = this.getInventory();

            CCScheduler.getInstance().getGlobalRegionScheduler().runTask(Main.instance, () -> this.player.openInventory(menu));
        });
    }
}
