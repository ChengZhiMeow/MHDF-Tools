package cn.chengzhiya.mhdftools.task.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.task.Task;

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
