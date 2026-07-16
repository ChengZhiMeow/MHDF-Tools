package cn.chengzhimeow.mhdftools.bukkit.common.action.pre;

import cn.chengzhimeow.ccaction.CCAction;
import cn.chengzhimeow.ccaction.action.AbstractAction;
import cn.chengzhimeow.ccaction.manager.PreProcessManager;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder.PlaceholderCompatibility;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder.PlaceholderCompatibilityRegistry;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class StringPlaceholderPreProcess implements PreProcessManager {
    @Override
    public Object handle(CCAction ccAction, AbstractAction abstractAction, Object o) {
        String s = (String) o;
        OfflinePlayer player = abstractAction.getParam("player", Player.class);

        return PlaceholderCompatibilityRegistry.getInstance().parseString(
                PlaceholderCompatibility.PlaceholderCompatibilityIds.PLACEHOLDER_API,
                player,
                s
        );
    }
}
