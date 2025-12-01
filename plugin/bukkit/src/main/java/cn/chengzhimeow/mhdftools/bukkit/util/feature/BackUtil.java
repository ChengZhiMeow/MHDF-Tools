package cn.chengzhimeow.mhdftools.bukkit.util.feature;

import cn.chengzhimeow.mhdftools.bukkit.config.file.ConfigSetting;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class BackUtil {
    /**
     * 获取指定玩家实例的位置数据数量
     *
     * @param player 玩家实例
     * @return 位置数据数量
     */
    public static int getMaxBack(Player player) {
        List<Integer> amountList = new ArrayList<>(player.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(permission -> permission.startsWith("mhdftools.commands.back.max."))
                .map(permission -> permission.substring("mhdftools.commands.back.max.".length()))
                .map(Integer::parseInt)
                .toList());
        amountList.sort(Comparator.reverseOrder());

        return !amountList.isEmpty() ? amountList.get(0) : ConfigSetting.getInstance().getData().getInt("backSettings.defaultMax");
    }
}
