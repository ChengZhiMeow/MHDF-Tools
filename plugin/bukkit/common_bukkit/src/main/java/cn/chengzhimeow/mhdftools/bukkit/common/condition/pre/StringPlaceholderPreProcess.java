package cn.chengzhimeow.mhdftools.bukkit.common.condition.pre;

import cn.chengzhimeow.cccondition.CCCondition;
import cn.chengzhimeow.cccondition.condition.AbstractCondition;
import cn.chengzhimeow.cccondition.manager.PreProcessManager;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder.PlaceholderCompatibility;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder.PlaceholderCompatibilityRegistry;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class StringPlaceholderPreProcess implements PreProcessManager {
    @Override
    public Object handle(CCCondition ccAction, AbstractCondition abstractCondition, Object o) {
        String s = (String) o;
        OfflinePlayer player = abstractCondition.getParam("player", Player.class);

        return PlaceholderCompatibilityRegistry.getInstance().parseString(
                PlaceholderCompatibility.PlaceholderCompatibilityIds.PLACEHOLDER_API,
                player,
                s
        );
    }
}
