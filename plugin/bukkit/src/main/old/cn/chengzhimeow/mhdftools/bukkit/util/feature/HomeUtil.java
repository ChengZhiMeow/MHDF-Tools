package cn.chengzhimeow.mhdftools.bukkit.util.feature;

import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class HomeUtil {
    /**
     * 获取指定玩家实例的家数量
     *
     * @param player 玩家实例
     * @return 家数量
     */
    public static int getMaxHome(Player player) {
        List<Integer> amountList = new ArrayList<>(player.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(permission -> permission.startsWith("mhdftools.commands.home.max."))
                .map(permission -> permission.substring("mhdftools.commands.home.max.".length()))
                .map(Integer::parseInt)
                .toList());
        amountList.sort(Comparator.reverseOrder());

        return !amountList.isEmpty() ? amountList.get(0) : ConfigSetting.getInstance().getData().getInt("homeSettings.defaultMax");
    }
}
