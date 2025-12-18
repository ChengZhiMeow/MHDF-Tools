package cn.chengzhimeow.mhdftools.bukkit.module.thread;

import lombok.Getter;

public final class MenuThread extends Thread {
    @Getter
    private static final MenuThread instance = new MenuThread();

    private MenuThread() {
        super("MHDF-Tools Menu Thread");
    }
}
