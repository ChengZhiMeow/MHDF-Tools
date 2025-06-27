package cn.chengzhiya.mhdftools.menu;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.YamlUtil;
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
public abstract class AbstractMenu implements InventoryHolder, Menu {
    private final boolean enable;
    private final Player player;

    public AbstractMenu(List<String> enableKeyList, Player player) {
        this.enable = YamlUtil.equalsTrue(ConfigUtil.getConfig(), enableKeyList);
        this.player = player;
    }

    public AbstractMenu(Player player) {
        this(new ArrayList<>(), player);
    }

    /**
     * 触发打开菜单事件的时候
     *
     * @param event 触发打开菜单事件
     */
    public void onOpen(InventoryOpenEvent event) {
        if (!isEnable()) {
            return;
        }

        open(event);
    }

    /**
     * 触发点击菜单事件的时候
     *
     * @param event 触发点击菜单事件
     */
    public void onClick(InventoryClickEvent event) {
        if (!isEnable()) {
            return;
        }

        click(event);
    }

    /**
     * 触发关闭菜单事件的时候
     *
     * @param event 触发关闭菜单事件
     */
    public void onClose(InventoryCloseEvent event) {
        if (!isEnable()) {
            return;
        }

        close(event);
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (!isEnable()) {
            return;
        }

        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            Inventory menu = getInventory();

            MHDFScheduler.getGlobalRegionScheduler().runTask(Main.instance, () -> player.openInventory(menu));
        });
    }
}
