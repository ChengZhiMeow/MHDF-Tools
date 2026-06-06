package cn.chengzhimeow.mhdftools.bukkit.module.quitmessage.listener;

import cn.chengzhimeow.mhdftools.bukkit.common.condition.ConditionManager;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder.PlaceholderCompatibility;
import cn.chengzhimeow.mhdftools.bukkit.compatibility.placeholder.PlaceholderCompatibilityRegistry;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.module.quitmessage.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.quitmessage.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.message.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

final class PlayerQuit extends Listener {
    public PlayerQuit() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable()
        );
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (ConfigSetting.getInstance().getConfig().removeMessage()) {
            event.quitMessage(null);
            return;
        }

        ConfigSetting.Config.Group lastGroup = null;
        int lastWeight = -1;
        for (ConfigSetting.Config.Group group : ConfigSetting.getInstance().getConfig().groups().values()) {
            if (!ConditionManager.getInstance().condition(player, group.conditions(), new HashMap<>())) continue;

            int weight = group.weight();
            if (weight <= lastWeight) continue;
            lastWeight = weight;
            lastGroup = group;
        }

        if (lastGroup == null) {
            event.quitMessage(null);
            return;
        }

        event.quitMessage(ColorUtil.color(
                PlaceholderCompatibilityRegistry.getInstance().get(PlaceholderCompatibility.PlaceholderCompatibilityIds.PLACEHOLDER_API)
                        .parseString(player, lastGroup.message())
        ));
    }
}
