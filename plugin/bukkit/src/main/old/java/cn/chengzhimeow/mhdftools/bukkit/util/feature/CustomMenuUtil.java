package cn.chengzhimeow.mhdftools.bukkit.util.feature;

import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.menu.feature.CustomMenu;
import org.bukkit.entity.Player;

public final class CustomMenuUtil {
    /**
     * 为指定玩家实例打开自定义菜单
     *
     * @param player 玩家实例
     * @param config 菜单配置实例
     */
    public static void openCustomMenu(Player player, ConfigurationSection config) {
        CustomMenu customMenu = new CustomMenu(player, config);
        customMenu.openMenu();
    }
}
