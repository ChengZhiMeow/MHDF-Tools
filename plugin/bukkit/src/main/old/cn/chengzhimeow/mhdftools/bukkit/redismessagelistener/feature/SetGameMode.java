package cn.chengzhimeow.mhdftools.bukkit.redismessagelistener.feature;

import cn.chengzhimeow.mhdftools.bukkit.message.LogUtil;
import cn.chengzhimeow.mhdftools.bukkit.redismessagelistener.RedisMessageListener;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;

public final class SetGameMode extends RedisMessageListener {
    public SetGameMode() {
        super(
                List.of("gamemodeSettings.enable"),
                "setGameMode"
        );
    }

    @Override
    public void onMessage(String message) {
        JSONObject data = JSONObject.parseObject(message);

        String playerName = data.getString("playerName");
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return;
        }

        String gamemode = data.getString("gameMode");
        LogUtil.debug("修改跨服游戏模式 | 目标玩家: {} | 游戏模式: {}",
                playerName,
                gamemode
        );
        player.setGameMode(GameMode.valueOf(gamemode));
    }
}
