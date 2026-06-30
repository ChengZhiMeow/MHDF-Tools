package cn.chengzhimeow.mhdftools.bukkit.module.fly.task;

import cn.chengzhimeow.mhdftools.api.MHDFToolsAPI;
import cn.chengzhimeow.mhdftools.api.entity.MHDFToolsPlayer;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Task;
import cn.chengzhimeow.mhdftools.bukkit.module.fly.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.fly.config.ConfigSetting;
import cn.chengzhimeow.mhdftools.bukkit.module.fly.config.LangSetting;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;

final class FlyTime extends Task {
    public FlyTime() {
        super(
                ModuleMain.instance,
                ConfigSetting.getInstance().getConfig().enable(),
                20L
        );
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getAllowFlight()) continue;

            MHDFToolsPlayer mhdfPlayer = MHDFToolsAPI.getInstance().getPlayerManager().getPlayer(player.getUniqueId(), player.getName());
            if (mhdfPlayer.isAllowedFlyingGameMode()) continue;
            if (player.hasPermission("mhdftools.commands.fly.infinite")) continue;

            long flyTime = mhdfPlayer.getFlyTime();
            if (flyTime <= ConfigSetting.getInstance().getConfig().fallTime()) {
                this.sendFallMessage(player, flyTime);
                this.playFallSound(player, flyTime);
            }

            if (flyTime <= 0) {
                mhdfPlayer.disableFly();
                continue;
            }

            mhdfPlayer.takeFlyTime(1);
        }
    }

    private void sendFallMessage(Player player, long flyTime) {
        LangSetting.Config.Commands.Fly.FallMessage message = LangSetting.getInstance().getConfig().commands().fly().fallMessages().get(flyTime);
        if (message == null) return;

        player.showTitle(Title.title(
                message.title(),
                message.subtitle(),
                Title.Times.times(
                        Duration.ofMillis(message.in() * 50L),
                        Duration.ofMillis(message.step() * 50L),
                        Duration.ofMillis(message.out() * 50L)
                )
        ));
    }

    private void playFallSound(Player player, long flyTime) {
        ConfigSetting.Config.FallSound sound = ConfigSetting.getInstance().getConfig().fallSounds().get(flyTime);
        if (sound == null || sound.sound().isBlank()) return;

        player.playSound(Sound.sound(
                Key.key(sound.sound()),
                Sound.Source.MASTER,
                sound.volume(),
                sound.pitch()
        ));
    }
}
