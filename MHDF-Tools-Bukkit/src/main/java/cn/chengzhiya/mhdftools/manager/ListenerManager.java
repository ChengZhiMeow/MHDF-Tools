package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.listener.AbstractPacketListener;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;

@SuppressWarnings("unused")
public final class ListenerManager {
    /**
     * 注册所有启用的监听器
     */
    @SneakyThrows
    public void init() {
            Reflections reflections = new Reflections(AbstractListener.class.getPackageName());

            for (Class<? extends AbstractPacketListener> clazz : reflections.getSubTypesOf(AbstractPacketListener.class)) {
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    AbstractPacketListener packetListener = clazz.getDeclaredConstructor().newInstance();
                    if (packetListener.isEnable()) {
                        Main.instance.getPluginHookManager().getPacketEventsHook()
                                .registerListener(packetListener, packetListener.getPriority());
                    }
                }
            }

            for (Class<? extends AbstractListener> clazz : reflections.getSubTypesOf(AbstractListener.class)) {
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    AbstractListener listener = clazz.getDeclaredConstructor().newInstance();
                    if (listener.isEnable()) {
                        Bukkit.getPluginManager().registerEvents(listener, Main.instance);
                    }
                }
            }
    }
}
