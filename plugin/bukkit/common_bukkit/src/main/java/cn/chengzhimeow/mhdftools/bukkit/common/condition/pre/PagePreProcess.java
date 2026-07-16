package cn.chengzhimeow.mhdftools.bukkit.common.condition.pre;

import cn.chengzhimeow.cccondition.CCCondition;
import cn.chengzhimeow.cccondition.condition.AbstractCondition;
import cn.chengzhimeow.cccondition.manager.PreProcessManager;
import cn.chengzhimeow.mhdftools.bukkit.common.menu.AbstractPageMenu;
import org.bukkit.entity.Player;

public final class PagePreProcess implements PreProcessManager {
    @Override
    public Object handle(CCCondition ccCondition, AbstractCondition condition, Object input) {
        if (!(input instanceof String text)) return input;
        if (text.equals("{page}") && condition.getParams().containsKey("page"))
            return condition.getParams().get("page");
        if (text.equals("{max_page}") && condition.getParams().containsKey("max_page"))
            return condition.getParams().get("max_page");

        Player player = condition.getParams().get("player") instanceof Player value ? value : null;
        if (player == null) return input;
        if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof AbstractPageMenu menu))
            return input;

        return switch (text) {
            case "{page}" -> menu.getPage();
            case "{max_page}" -> menu.maxPage();
            default -> input;
        };
    }
}
