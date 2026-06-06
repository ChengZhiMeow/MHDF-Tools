package cn.chengzhimeow.mhdftools.bukkit.common.action.ext;

import cn.chengzhimeow.ccaction.CCAction;
import cn.chengzhimeow.ccaction.action.AbstractAction;
import cn.chengzhimeow.ccaction.action.ArgumentKey;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.AbstractPageMenu;
import org.bukkit.entity.Player;

import java.util.Map;

public final class PrevPageActionImpl extends AbstractAction {
    @ArgumentKey(keys = {"player", "p"}, disabledCheck = true)
    private Player player;

    public PrevPageActionImpl(CCAction ccAction, Map<String, Object> params) {
        super(ccAction, params);
    }

    @Override
    protected void onAction() {
        if (!(this.player.getOpenInventory().getTopInventory().getHolder() instanceof AbstractPageMenu menu)) return;
        menu.setPage(menu.getPage() - 1);
        menu.openInventory(this.player);
    }
}
