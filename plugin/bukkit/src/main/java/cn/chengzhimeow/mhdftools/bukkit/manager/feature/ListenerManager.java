package cn.chengzhimeow.mhdftools.bukkit.manager.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.PacketListener;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@SuppressWarnings("unused")
public final class ListenerManager {
    /**
     * 注册所有启用的监听器
     */
    @SneakyThrows
    public void init() {
        Reflections reflections = new Reflections(Listener.class.getPackageName());

        for (Class<? extends PacketListener> clazz : reflections.getSubTypesOf(PacketListener.class)) {
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                Constructor<? extends PacketListener> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                PacketListener listener = constructor.newInstance();

                if (listener.isEnable()) {
                    Main.instance.getPluginHookManager().getPacketEventsHook()
                            .registerListener(listener, listener.getPriority());
                }
            }
        }

        for (Class<? extends Listener> clazz : reflections.getSubTypesOf(Listener.class)) {
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                Constructor<? extends Listener> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                Listener listener = constructor.newInstance();

                if (listener.isEnable()) {
                    Bukkit.getPluginManager().registerEvents(listener, Main.instance);
                }
            }
        }
    }
}
