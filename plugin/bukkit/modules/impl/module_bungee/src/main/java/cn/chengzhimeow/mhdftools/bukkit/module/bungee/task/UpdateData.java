package cn.chengzhimeow.mhdftools.bukkit.module.bungee.task;

import cn.chengzhimeow.mhdftools.bukkit.module.bungee.ModuleMain;
import cn.chengzhimeow.mhdftools.bukkit.module.bungee.manager.BungeeCordManagerImpl;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Task;

import java.util.List;

final class UpdateData extends Task {
    public UpdateData() {
        super(
                ModuleMain.instance,
                List.of("enable"),
                20
        );
    }

    @Override
    public void run() {
        BungeeCordManagerImpl.getInstance().updateData();
    }
}
