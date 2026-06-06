package cn.chengzhimeow.mhdftools.bukkit.common.menu;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public abstract class AbstractPageMenu extends AbstractMenu {
    private int page;

    public AbstractPageMenu(@NotNull Player player, int page) {
        super(player);
        this.page = page;
    }

    public AbstractPageMenu(@NotNull Player player) {
        this(player, 1);
    }

    public abstract int maxPage();

    public void openInventory(@NotNull Player player, int page) {
        this.page = page;
        super.updateInventory();
        super.openInventory(player);
    }

    public void openInventory(int page) {
        this.page = page;
        this.updateInventory();
        this.openInventory();
    }
}
