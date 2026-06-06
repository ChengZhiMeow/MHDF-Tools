package cn.chengzhimeow.mhdftools.bukkit.module.vanish.config;

import lombok.Getter;
import net.kyori.adventure.bossbar.BossBar;

public final class BossBarSetting {
    @Getter(lazy = true)
    private static final BossBarSetting instance = new BossBarSetting();
    @Getter private BossBar vanish;

    private BossBarSetting() {
    }

    public void reload() {
        ConfigSetting.Config.Bossbar.Vanish config = ConfigSetting.getInstance().getConfig().bossbar().vanish();
        if (this.vanish != null) {
            this.vanish.name(LangSetting.getInstance().getConfig().bossbar().vanish());
            this.vanish.color(config.color());
            return;
        }

        this.vanish = BossBar.bossBar(
                LangSetting.getInstance().getConfig().bossbar().vanish(),
                BossBar.MAX_PROGRESS,
                config.color(),
                BossBar.Overlay.PROGRESS
        );
    }
}
