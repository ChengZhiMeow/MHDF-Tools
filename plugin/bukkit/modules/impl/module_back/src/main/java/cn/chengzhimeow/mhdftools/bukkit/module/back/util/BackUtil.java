package cn.chengzhimeow.mhdftools.bukkit.module.back.util;

import cn.chengzhimeow.mhdftools.bukkit.module.back.config.ConfigSetting;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class BackUtil {
    public static int getMaxBack(Player player) {
        List<Integer> amountList = new ArrayList<>(player.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(permission -> permission.startsWith("mhdftools.commands.back.max."))
                .map(permission -> permission.substring("mhdftools.commands.back.max.".length()))
                .filter(value -> value.matches("\\d+"))
                .map(Integer::parseInt)
                .toList());
        amountList.sort(Comparator.reverseOrder());

        return !amountList.isEmpty() ? amountList.get(0) : ConfigSetting.getInstance().getConfig().defaultMax();
    }

    private BackUtil() {
    }
}
