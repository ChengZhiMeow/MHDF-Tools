package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.menu.feature.CustomMenu;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public final class CustomMenuUtil {
    /**
     * 为指定玩家实例打开自定义菜单
     *
     * @param player 玩家实例
     * @param config 菜单配置实例
     */
    public static void openCustomMenu(Player player, YamlConfiguration config) {
        CustomMenu customMenu = new CustomMenu(player, config);
        customMenu.openMenu();
    }
}
