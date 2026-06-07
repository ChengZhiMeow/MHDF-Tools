package cn.chengzhimeow.mhdftools.bukkit.module.custommenu.action;

import cn.chengzhimeow.ccaction.CCAction;
import cn.chengzhimeow.ccaction.action.AbstractAction;
import cn.chengzhimeow.ccaction.action.ArgumentKey;
import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.config.MenuSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.custommenu.menu.CustomMenu;
import org.bukkit.entity.Player;

import java.util.Map;

public final class OpenCustomMenuAction extends AbstractAction {
    @ArgumentKey(keys = {"player", "p"}, disabledCheck = true)
    private Player player;
    @ArgumentKey(keys = {"menu", "id"})
    private String menu;

    public OpenCustomMenuAction(CCAction ccAction, Map<String, Object> params) {
        super(ccAction, params);
    }

    @Override
    protected void onAction() {
        MenuSetting.Config.Menu config = MenuSetting.getInstance().getMenu(this.menu);
        if (config == null) return;

        new CustomMenu(this.player, config).openInventory();
    }
}
