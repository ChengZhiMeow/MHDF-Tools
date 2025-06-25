package cn.chengzhiya.mhdftools.task.feature;

import cn.chengzhiya.mhdftools.entity.database.data.FlyStatus;
import cn.chengzhiya.mhdftools.task.AbstractTask;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.config.SoundUtil;
import cn.chengzhiya.mhdftools.util.database.FlyStatusUtil;
import cn.chengzhiya.mhdftools.util.feature.FlyUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("unused")
public final class FlyTime extends AbstractTask {
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
            if (!player.getAllowFlight()) {
                return;
            }

            // 不处理可以飞行的游戏模式
            if (FlyUtil.isAllowedFlyingGameMode(player)) {
                return;
            }

            // 不处理永久飞行的玩家
            if (player.hasPermission("mhdftools.commands.fly.infinite")) {
                return;
            }

            FlyStatus flyStatus = FlyStatusUtil.getFlyStatus(player);

            // 发送迫降提示
            String title = LangUtil.getString("commands.fly.fallMessage." + flyStatus.getTime());
            if (!title.isEmpty()) {
                String[] args = title.split("\\|");
                ActionUtil.sendTitle(player, args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
            }

            // 播放音效
            String sound = SoundUtil.getSound("flyFall." + flyStatus.getTime());
            if (!sound.isEmpty()) {
                String[] args = sound.split("\\|");
                ActionUtil.playSound(player, args[0], Float.parseFloat(args[1]), Float.parseFloat(args[2]));
            }

            if (flyStatus.getTime() <= 0) {
                FlyUtil.disableFly(player);
                return;
            }

            // 减少飞行时长
            flyStatus.setTime(flyStatus.getTime() - 1);
            FlyStatusUtil.updateFlyStatus(flyStatus);
        }
    }
}
