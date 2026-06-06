package cn.chengzhimeow.mhdftools.bukkit.task.feature;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.config.file.LangSetting;
import cn.chengzhimeow.mhdftools.bukkit.config.file.SoundSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Task;
import cn.chengzhimeow.mhdftools.bukkit.util.action.ActionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("unused")
final class FlyTime extends Task {
    public FlyTime() {
        super(
                List.of("flySettings.enable"),
                20L
        );
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            // 不处理不可飞行的玩家
            if (!player.getAllowFlight()) return;

            // 不处理可以飞行的游戏模式
            MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player);
            if (mhdfPlayer.isAllowedFlyingGameMode()) return;

            // 不处理永久飞行的玩家
            if (player.hasPermission("mhdftools.commands.fly.infinite")) return;

            // 发送迫降提示
            String title = LangSetting.getInstance().getData().getString("commands.fly.fallMessage." + mhdfPlayer.getFlyTime());
            if (title != null && !title.isEmpty()) {
                String[] args = title.split("\\|");
                ActionUtil.sendTitle(player, args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
            }

            // 播放音效
            String sound = SoundSetting.getInstance().getData().getString("flyFall." + mhdfPlayer.getFlyTime());
            if (sound != null && !sound.isEmpty()) {
                String[] args = sound.split("\\|");
                ActionUtil.playSound(player, args[0], Float.parseFloat(args[1]), Float.parseFloat(args[2]));
            }

            if (mhdfPlayer.getFlyTime() <= 0) {
                mhdfPlayer.disableFly();
                return;
            }

            // 减少飞行时长
            mhdfPlayer.takeFlyTime(1);
        }
    }
}
