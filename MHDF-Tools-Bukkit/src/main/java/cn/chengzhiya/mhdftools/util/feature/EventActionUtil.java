package cn.chengzhiya.mhdftools.util.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.GroupUtil;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.message.LogUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class EventActionUtil {
    /**
     * 获取指定事件的操作列表
     *
     * @param event 事件
     * @return 操作列表
     */
    private static List<String> getActionList(Player player, String event) {
        ConfigurationSection actionList = Main.instance.getConfigManager().getConfigManager().getData().getConfigurationSection("eventActionSettings.actionList");
        if (actionList == null) {
            return new ArrayList<>();
        }

        List<String> list = new ArrayList<>();
        for (String key : actionList.getKeys(false)) {
            ConfigurationSection action = actionList.getConfigurationSection(key);
            if (action == null) {
                continue;
            }

            String type = action.getString("event");
            if (type == null) {
                continue;
            }

            LogUtil.debug("事件操作类型比对 | 事件名称: {} | 事件类型: {} | 目标类型: {}",
                    key,
                    type,
                    event
            );

            if (!type.equals(event)) {
                continue;
            }
            List<String> world = action.getStringList("world");
            if (!world.isEmpty()) {
                LogUtil.debug("世界白名单比对 | 事件名称: {} | 要求所在世界: {} | 玩家所在世界: {}",
                        key,
                        String.valueOf(world),
                        player.getWorld().getName()
                );

                if (!world.contains(player.getWorld().getName())) {
                    continue;
                }
            }

            ConfigurationSection group = action.getConfigurationSection("group");
            if (group == null) {
                continue;
            }

            ConfigurationSection playerGroup = group.getConfigurationSection(GroupUtil.getGroup(player, group, "mhdftools.group.eventaction." + key + "."));
            if (playerGroup == null) {
                continue;
            }

            list.addAll(playerGroup.getStringList("action"));
        }

        LogUtil.debug("对应事件类型操作列表获取成功 | 事件类型: {} | 操作列表: {}",
                event,
                String.valueOf(list)
        );
        return list;
    }

    /**
     * 执行操作
     *
     * @param player 玩家实例
     * @param event  事件
     */
    public static void runAction(Player player, String event) {
        ActionUtil.runActionList(player, EventActionUtil.getActionList(player, event));
    }
}
