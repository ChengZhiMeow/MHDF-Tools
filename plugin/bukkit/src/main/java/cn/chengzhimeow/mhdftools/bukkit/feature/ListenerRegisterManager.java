package cn.chengzhimeow.mhdftools.bukkit.feature;

import cn.chengzhimeow.mhdftools.bukkit.Main;
import cn.chengzhimeow.mhdftools.bukkit.module.Module;
import cn.chengzhimeow.mhdftools.bukkit.module.feature.Listener;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class ListenerRegisterManager {
    @Getter(lazy = true)
    private static final ListenerRegisterManager instance = new ListenerRegisterManager();

    private ListenerRegisterManager() {
    }

    @SneakyThrows
    public void registerListeners(Module module) {
        Reflections reflections = new Reflections(module.getClass().getPackageName());

        Set<Class<? extends Listener>> listenerClassSet = reflections.getSubTypesOf(Listener.class).stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .sorted(Comparator.comparing(Class::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (Class<? extends Listener> clazz : listenerClassSet) {
            Constructor<? extends Listener> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Listener listener = constructor.newInstance();

            if (!listener.isEnable()) continue;

            Bukkit.getPluginManager().registerEvents(listener, Main.instance);
        }
    }
}
