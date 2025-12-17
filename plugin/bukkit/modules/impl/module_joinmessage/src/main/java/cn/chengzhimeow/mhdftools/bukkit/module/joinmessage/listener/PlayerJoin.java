package cn.chengzhimeow.mhdftools.bukkit.module.joinmessage.listener;

import cn.chengzhimeow.cccondition.condition.ConditionBuilder;
import cn.chengzhimeow.ccyaml.configuration.ConfigurationSection;
import cn.chengzhimeow.mhdftools.bukkit.common.condition.ConditionManager;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder.PlaceholderCompatibility;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder.PlaceholderCompatibilityRegistry;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.module.joinmessage.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.joinmessage.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

final class PlayerJoin extends Listener {
    public PlayerJoin() {
        super(
                ModuleMain.instance,
                List.of("enable")
        );
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // 移除消息
        if (ConfigSetting.getInstance().getData().getBoolean("remove_message")) {
            event.joinMessage(null);
            return;
        }

        ConfigurationSection config = ConfigSetting.getInstance().getData().getConfigurationSection("groups");
        if (config == null) return;

        ConfigurationSection lastGroup = null;
        int lastWeight = -1;
        for (String key : config.getKeys(false)) {
            ConfigurationSection group = config.getConfigurationSection(key);
            if (group == null) continue;

            // 检查是否满足条件
            List<ConditionBuilder.Builder> conditions = ConditionManager.getInstance().getConditionListFromConfig(group, "conditions");
            if (!ConditionManager.getInstance().condition(player, conditions)) continue;

            // 检查是否是更大的权重
            int weight = group.getInt("weight");
            if (weight <= lastWeight) continue;
            lastWeight = weight;
            lastGroup = group;
        }

        if (lastGroup == null) {
            event.joinMessage(null);
            return;
        }

        event.joinMessage(ColorUtil.color(
                PlaceholderCompatibilityRegistry.getInstance().get(PlaceholderCompatibility.PlaceholderCompatibilityIds.PLACEHOLDER_API)
                        .parseString(player, lastGroup.getString("message"))
        ));
    }
}
