package cn.chengzhiya.mhdftools.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;
import java.util.Locale;

public final class GroupUtil {
    /**
     * 获取指定玩家所在的组
     *
     * @param player           玩家实例
     * @param config           配置实例
     * @param permissionPrefix 权限前缀
     * @return 组名称
     */
    public static String getGroup(Player player, ConfigurationSection config, String permissionPrefix) {
        if (config == null || permissionPrefix == null) return "default";
        permissionPrefix = permissionPrefix.toLowerCase(Locale.ROOT);

        String finalPermissionPrefix = permissionPrefix;
        List<String> groupList = player.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(permission -> permission.startsWith(finalPermissionPrefix))
                .map(permission -> permission.substring(finalPermissionPrefix.length()))
                .toList();

        String lastMaxWeightGroup = "default";
        int lastMaxWeight = 0;
        for (String group : groupList) {
            int weight = config.getInt(group + ".weight");

            if (weight <= lastMaxWeight) {
                continue;
            }

            lastMaxWeight = weight;
            lastMaxWeightGroup = group;
        }

        return lastMaxWeightGroup;
    }
}
