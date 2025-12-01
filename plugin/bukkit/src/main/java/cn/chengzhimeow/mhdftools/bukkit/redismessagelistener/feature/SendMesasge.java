package cn.chengzhimeow.mhdftools.bukkit.redismessagelistener.feature;

import cn.chengzhimeow.mhdftools.bukkit.redismessagelistener.RedisMessageListener;
import cn.chengzhimeow.mhdftools.text.TextComponent;
import cn.chengzhimeow.mhdftools.bukkit.message.LogUtil;
import com.alibaba.fastjson2.JSONObject;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class SendMesasge extends RedisMessageListener {
    public SendMesasge() {
        super(
                "sendMessage"
        );
    }

    @Override
    public void onMessage(String message) {
        JSONObject data = JSONObject.parseObject(message);

        String playerName = data.getString("playerName");
        String text = data.getString("message");
        LogUtil.debug("发送跨服消息 | 目标玩家: {} | 消息: {}",
                playerName,
                text
        );

        TextComponent textComponent = new TextComponent(JSONComponentSerializer.json().deserialize(text));

        if (playerName.equals("console")) {
            LogUtil.log(textComponent.toLegacyString());
            return;
        }

        if (playerName.equals("all")) {
            ActionUtil.broadcastMessage(textComponent);
            return;
        }

        Player player = Bukkit.getPlayer(playerName);
        if (player == null) return;
        player.sendMessage(textComponent);
    }
}
