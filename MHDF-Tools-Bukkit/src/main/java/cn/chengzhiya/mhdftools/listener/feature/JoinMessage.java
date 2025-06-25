package cn.chengzhiya.mhdftools.listener.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.text.TextComponent;
import cn.chengzhiya.mhdftools.util.GroupUtil;
import cn.chengzhiya.mhdftools.util.PluginUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.feature.NickUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public final class JoinMessage extends AbstractListener {
    public JoinMessage() {
        super(
                List.of("joinMessageSettings.enable")
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ConfigurationSection config = ConfigUtil.getConfig().getConfigurationSection("joinMessageSettings");
        if (config == null) {
            return;
        }

        if (config.getBoolean("removeMessage")) {
            setJoinMessage(event, null);
            return;
        }

        String message = config.getString(GroupUtil.getGroup(player, config, "mhdftools.group.joinmessage.") + ".message");
        if (message == null) {
            setJoinMessage(event, null);
            return;
        }

        setJoinMessage(event, ColorUtil.color(Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(player, message))
                .replace("{player}", NickUtil.getName(player))
        );
    }

    /**
     * 设置进服提示
     *
     * @param event   进服事件实例
     * @param message 文本实例
     */
    private void setJoinMessage(PlayerJoinEvent event, TextComponent message) {
        if (PluginUtil.isNativeSupportAdventureApi()) {
            event.joinMessage(message);
        } else {
            event.setJoinMessage(message.toLegacyString());
        }
    }
}
