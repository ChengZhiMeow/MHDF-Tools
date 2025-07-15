package cn.chengzhiya.mhdftools.manager.feature;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.listener.AbstractListener;
import cn.chengzhiya.mhdftools.listener.AbstractPacketListener;
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
        Reflections reflections = new Reflections(AbstractListener.class.getPackageName());

        for (Class<? extends AbstractPacketListener> clazz : reflections.getSubTypesOf(AbstractPacketListener.class)) {
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                Constructor<? extends AbstractPacketListener> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                AbstractPacketListener listener = constructor.newInstance();

                if (listener.isEnable()) {
                    Main.instance.getPluginHookManager().getPacketEventsHook()
                            .registerListener(listener, listener.getPriority());
                }
            }
        }

        for (Class<? extends AbstractListener> clazz : reflections.getSubTypesOf(AbstractListener.class)) {
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                Constructor<? extends AbstractListener> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                AbstractListener listener = constructor.newInstance();

                if (listener.isEnable()) {
                    Bukkit.getPluginManager().registerEvents(listener, Main.instance);
                }
            }
        }
    }
}
