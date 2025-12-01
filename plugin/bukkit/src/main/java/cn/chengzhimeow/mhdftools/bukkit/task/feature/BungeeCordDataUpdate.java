package cn.chengzhimeow.mhdftools.bukkit.task.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Task;

import java.util.List;

@SuppressWarnings("unused")
final class BungeeCordDataUpdate extends Task {
    public BungeeCordDataUpdate() {
        super(
                List.of("bungeeCordSettings.enable"),
                20L
        );
    }

    @Override
    public void run() {
        Main.instance.getBungeeCordManager().updateServerName();
        Main.instance.getBungeeCordManager().updateBungeeCordPlayerList();
    }
}
